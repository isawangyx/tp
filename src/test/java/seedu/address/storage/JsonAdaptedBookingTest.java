package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedBooking.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalBookings.BENSONS_BOOKING;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.booking.Booking;

public class JsonAdaptedBookingTest {
    private static final String INVALID_TIME = "INVALID DATE";
    private static final String INVALID_STATUS = "INVALID";
    private static final int INVALID_PAX = -1;

    private static final String VALID_TIME = BENSONS_BOOKING.getBookingDateTime().toString();
    private static final String VALID_STATUS = BENSONS_BOOKING.getStatus().toString();
    private static final int VALID_PAX = BENSONS_BOOKING.getPax();
    private static final String VALID_REMARKS = BENSONS_BOOKING.getRemarks();

    @Test
    public void toModelType_validBookingDetails_returnsBooking() throws Exception {
        JsonAdaptedBooking booking = new JsonAdaptedBooking(BENSONS_BOOKING);
        Booking modelTypeBooking = booking.toModelType();
        modelTypeBooking.setBookingPerson(BENSON);
        assertEquals(BENSONS_BOOKING, modelTypeBooking);
    }

    @Test
    public void toModelType_invalidBookingDateTime_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, INVALID_TIME, INVALID_TIME, VALID_STATUS, VALID_REMARKS, VALID_PAX);
        String expectedMessage = "Text 'INVALID DATE' could not be parsed at index 0";
        assertThrows(java.time.format.DateTimeParseException.class, expectedMessage, booking::toModelType);
    }

    @Test
    public void toModelType_invalidBookingMadeDateTime_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, INVALID_TIME, INVALID_TIME, VALID_STATUS, VALID_REMARKS, VALID_PAX);
        String expectedMessage = "Text 'INVALID DATE' could not be parsed at index 0";
        assertThrows(java.time.format.DateTimeParseException.class, expectedMessage, booking::toModelType);
    }


    @Test
    public void toModelType_invalidStatus_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, VALID_TIME, VALID_TIME, INVALID_STATUS, VALID_REMARKS, VALID_PAX);
        String expectedMessage = "Status should be either 'Upcoming', 'Cancelled' or 'Completed'";
        assertThrows(IllegalValueException.class, expectedMessage, booking::toModelType);
    }

    @Test
    public void toModelType_invalidPax_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, VALID_TIME, VALID_TIME, VALID_STATUS, VALID_REMARKS, INVALID_PAX);
        String expectedMessage = "Pax should be a non-zero positive integer less than 501";
        assertThrows(IllegalValueException.class, expectedMessage, booking::toModelType);
    }

    @Test
    public void toModelType_nullBookingDateTime_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, null, VALID_TIME, VALID_STATUS, VALID_REMARKS, VALID_PAX);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDateTime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, booking::toModelType);
    }

    @Test
    public void toModelType_nullBookingMadeDateTime_throwsIllegalValueException() {
        JsonAdaptedBooking booking =
                new JsonAdaptedBooking(0, VALID_TIME, null, VALID_STATUS, VALID_REMARKS, VALID_PAX);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDateTime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, booking::toModelType);
    }
}
