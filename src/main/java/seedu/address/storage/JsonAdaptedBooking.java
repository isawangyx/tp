package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.booking.Booking;
import seedu.address.model.booking.Status;
import seedu.address.model.person.Person;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedBooking {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Booking's %s field is missing!";

    private final Integer bookingId;
    private final String bookingDate;
    private final String bookingMadeDate;
    private final String status;
    private final String remarks;
    private final Integer pax;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedBooking(@JsonProperty("bookingId") int bookingId,
                              @JsonProperty("bookingDate") String bookingDate,
                              @JsonProperty("bookingMadeDate") String bookingMadeDate,
                              @JsonProperty("status") String status,
                              @JsonProperty("remarks") String remarks,
                              @JsonProperty("pax") int pax) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingMadeDate = bookingMadeDate;
        this.status = status;
        this.remarks = remarks;
        this.pax = pax;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedBooking(Booking source) {
        bookingId = source.getBookingId();
        bookingDate = source.getBookingDateTime().toString();
        bookingMadeDate = source.getBookingMadeDateTime().toString();
        status = source.getStatus().toString();
        remarks = source.getRemarks();
        pax = source.getPax();
    }

    /**
     * Converts this Jackson-friendly adapted booking object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Booking toModelType() throws IllegalValueException {
        if (bookingId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Integer.class.getSimpleName()));
        }

        final int modelBookingId = bookingId;

        if (bookingDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalDateTime.class.getSimpleName()));
        }
        final LocalDateTime modelBookingDate = LocalDateTime.parse(bookingDate);

        if (bookingMadeDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalDateTime.class.getSimpleName()));
        }
        final LocalDateTime modelBookingMadeDate = LocalDateTime.parse(bookingMadeDate);

        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Status.class.getSimpleName()));
        }
        if (!Status.isValidStatus(status)) {
            throw new IllegalValueException(Status.MESSAGE_CONSTRAINTS);
        }
        final Status modelStatus = Status.fromString(status);

        if (remarks == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    String.class.getSimpleName()));
        }
        final String modelRemarks = remarks;

        if (pax == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Integer.class.getSimpleName()));
        }
        if (pax < 1 || pax > 500) {
            throw new IllegalValueException(String.format("Pax should be a non-zero positive integer less than 501",
                    Integer.class.getSimpleName()));
        }
        final int modelPax = pax;

        return new Booking(modelBookingId, modelBookingDate, modelBookingMadeDate, modelStatus,
                modelRemarks, modelPax);
    }

}
