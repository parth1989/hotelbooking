package com.m3bi.service;

import static com.m3bi.enums.BookingStatusEnum.BOOKED;
import static com.m3bi.enums.BookingStatusEnum.CANCELLED;
import static com.m3bi.enums.BookingStatusEnum.PENDING_APPROVAL;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.m3bi.dao.HotelBookingRepository;
import com.m3bi.dao.HotelRepository;
import com.m3bi.dao.HotelRoomRepository;
import com.m3bi.dao.HotelRoomTypeRepository;
import com.m3bi.dao.UserRepository;
import com.m3bi.exception.HotelNotFoundException;
import com.m3bi.exception.RoomTypeNotFoundException;
import com.m3bi.exception.UserNotFoundException;
import com.m3bi.model.Hotel;
import com.m3bi.model.HotelBookingRequest;
import com.m3bi.model.HotelRoom;
import com.m3bi.model.HotelRoomBooking;
import com.m3bi.model.RoomType;
import com.m3bi.model.User;

@Service
public class HotelBookingServiceImpl implements HotelBookingService {

	@Autowired
	private HotelBookingRepository hotelBookingDAO;

	@Autowired
	private HotelRepository hotelRepo;

	@Autowired
	private HotelRoomTypeRepository hotelRoomTypeDAO;

	@Autowired
	private HotelRoomRepository hotelRoomRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public void bookHotelRoom(HotelBookingRequest hotelBookingRequest)
			throws HotelNotFoundException, RoomTypeNotFoundException, UserNotFoundException, Exception {
		       
		        // check hotel with name exists
				String hotelName = hotelBookingRequest.getHotelName();
				Hotel hotel = hotelRepo.findByName(hotelName);
				if(hotel == null) {
					throw new HotelNotFoundException("Hotel "+hotelName+" not found.");
				}
				// check hotel room type exists
				String hotelRoomType = hotelBookingRequest.getRoomType();
				RoomType roomType = hotelRoomTypeDAO.findByRoomType(hotelRoomType);
				if(roomType == null) {
					throw new RoomTypeNotFoundException("Room type " + hotelRoomType + " not found.");
				}
				// check user exists in the db
				int userId = hotelBookingRequest.getUserId();
				Optional<User> userObj = userRepo.findById(userId);
				if(!userObj.isPresent()) {
					throw new UserNotFoundException("User with id "+userId+" not found.");
				}
				User user = userObj.get();
				// check booking date is not older date than today
				Date bookingDate = hotelBookingRequest.getBookingDate();
				long millis = System.currentTimeMillis();
				Date currentDate = new Date(millis);
				if(bookingDate.toLocalDate().isBefore(currentDate.toLocalDate())) {
					throw new Exception("Date should be greater than now or current");
				}
	
				HotelRoomBooking hotelRoomBooking = new HotelRoomBooking();
				hotelRoomBooking.setBookingDate(bookingDate);
				String bookingStatus = (roomType.getCost().intValue() <= user.getBonusPoints()) ?
						BOOKED.name() : PENDING_APPROVAL.name();
				hotelRoomBooking.setBookingStatus(bookingStatus);
				hotelRoomBooking.setHotelId(hotel.getId());
				hotelRoomBooking.setHotelRoomType(hotelRoomType);
				hotelRoomBooking.setUserId(userId);
				
				HotelRoom hotelRoom = null;
				// check empty hotel rooms
				List<HotelRoom> hotelRooms = hotelRoomRepo.findByHotelIdAndRoomTypeId(hotel.getId(), roomType.getId());
				List<HotelRoomBooking> hotelRoomBookings = hotelBookingDAO.findByHotelIdAndBookingDateAndHotelRoomType(hotel.getId(), bookingDate, hotelRoomType);
				List<HotelRoom> vacantHotelRooms = new ArrayList<>();
				vacantHotelRooms.addAll(hotelRooms);
				
				// check hotel room is already booked or in pending approval before we proceed new booking.
				for(HotelRoomBooking booking:hotelRoomBookings) {
					if(BOOKED.name().equals(booking.getBookingStatus()) || PENDING_APPROVAL.name().equals(booking.getBookingStatus())) {
						vacantHotelRooms.removeAll(hotelRooms.parallelStream().filter((room) -> booking.getHotelRoomId() == room.getId()).collect(Collectors.toList()));
					}
				}
				
				// check if hotel rooms are empty and probable booking status is booked.
				if((vacantHotelRooms == null || vacantHotelRooms.isEmpty()) && BOOKED.name().equals(bookingStatus)) {
					// check pending approval hotel rooms and cancel the current booking and assign it to this booking.
					List<HotelRoomBooking> pendingApprovalHotelRoomBookings = hotelRoomBookings.parallelStream().filter((booking) -> PENDING_APPROVAL.name().equals(booking.getBookingStatus())).collect(Collectors.toList()); //hotelBookingDAO.findByHotelIdAndBookingDateAndBookingStatusAndHotelRoomType(hotel.getId(), bookingDate, PENDING_APPROVAL.name(), hotelRoomType);
					if(pendingApprovalHotelRoomBookings != null && pendingApprovalHotelRoomBookings.size() > 0) {
						HotelRoomBooking pendingApprovalHotelRoomBooking = pendingApprovalHotelRoomBookings.get(0);
						hotelRoom = hotelRooms.parallelStream().filter((room) -> pendingApprovalHotelRoomBooking.getHotelRoomId() == room.getId()).collect(Collectors.toList()).get(0);
						pendingApprovalHotelRoomBooking.setBookingStatus(CANCELLED.name());
						hotelBookingDAO.save(pendingApprovalHotelRoomBooking);
					} else {
						throw new Exception("Hotel rooms in " + hotelName +" of type " + roomType.getRoomType() +" are not available.");
					}
				} else {
					hotelRoom = vacantHotelRooms.get(0);
				}
				hotelRoomBooking.setHotelRoomId(hotelRoom.getId());
				hotelRoomBooking = hotelBookingDAO.save(hotelRoomBooking);
				
				// assign a hotel room and update booking object
//				hotelRoom.setBookingStatus(bookingStatus);
//				hotelRoomRepo.save(hotelRoom);
				// deduct the bonus points from user for this booking if status is BOOKED.
				if(BOOKED.name().equals(bookingStatus)) {
					int remainingUserBonusPoints = user.getBonusPoints() - roomType.getCost().intValue();
					user.setBonusPoints(remainingUserBonusPoints);
					userRepo.save(user);
				}
	}

}
