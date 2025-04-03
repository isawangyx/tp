package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.booking.Booking;
import seedu.address.model.booking.Status;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Changes the remark of an existing person in the address book.
 */
public class FilterBookingsCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters the bookings by phone number, date, status or all.\n"
            + "Parameters: [p/PHONE_NUMBER] [d/DATE] [s/STATUS]\n"
            + "At least one parameter must be provided.\n"
            + "Example 1: " + COMMAND_WORD + " p/98765432\n"
            + "Example 2: " + COMMAND_WORD + " d/2023-12-25\n"
            + "Example 3: " + COMMAND_WORD + " s/COMPLETED\n"
            + "Example 4: " + COMMAND_WORD + " p/98765432 d/2023-12-25 s/upcoming";

    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with phone number: %s";
    public static final String MESSAGE_NO_BOOKINGS = "No bookings found%s.";
    public static final String MESSAGE_SUCCESS = "Here are the bookings%s:";

    private final Phone phoneNumber;
    private final LocalDateTime bookingDate;
    private final Status status;

    /**
     * Creates a Filter Command to list the bookings of specified {@code Person}
     */
    public FilterBookingsCommand(Phone phoneNumber, LocalDateTime bookingDate, Status status) {
        this.phoneNumber = phoneNumber;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        AddressBook addressBook = (AddressBook) model.getAddressBook();

        Predicate<Booking> predicate = booking -> true;
        String filterDescription = "";

        if (phoneNumber != null) {
            // Find person by phone number
            Person person = addressBook.getPersonByPhone(phoneNumber);

            if (person == null) {
                throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, phoneNumber));
            }

            predicate = predicate.and(booking -> person.getBookingIDs().contains(booking.getBookingId()));
            filterDescription = " for phone number " + phoneNumber;
        }

        if (bookingDate != null) {
            // Compare only the date part (year, month, day) ignoring time
            predicate = predicate.and(booking ->
                    booking.getBookingDateTime().toLocalDate().equals(bookingDate.toLocalDate()));

            String formattedDate = bookingDate.toLocalDate().format(
                    DateTimeFormatter.ofPattern("dd MMM yyyy"));

            filterDescription = phoneNumber != null
                    ? filterDescription + " on " + formattedDate
                    : " on " + formattedDate;
        }

        if (status != null) {
            predicate = predicate.and(booking -> booking.getStatus().equals(status));
            filterDescription = filterDescription + " with status " + status;
        }

        model.updateFilteredBookingList(predicate);

        if (model.getFilteredBookingList().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_BOOKINGS, filterDescription));
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, filterDescription));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FilterBookingsCommand
                && Objects.equals(phoneNumber, ((FilterBookingsCommand) other).phoneNumber)
                && Objects.equals(bookingDate, ((FilterBookingsCommand) other).bookingDate)
                && Objects.equals(status, ((FilterBookingsCommand) other).status));
    }
}
