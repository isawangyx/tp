package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOKING_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_UPCOMING_BOOKINGS;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.booking.Booking;
import seedu.address.model.person.Person;

/**
 * Edits the details of an existing booking in the booking list.
 */
public class EditBookingCommand extends Command {

    public static final String COMMAND_WORD = "bedit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the booking identified "
            + "by the booking ID.\n"
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + PREFIX_BOOKING_ID + "BOOKING_ID "
            + "[" + PREFIX_DATE + "DATETIME] "
            + "[" + PREFIX_PAX + "PAX] "
            + "[" + PREFIX_REMARK + "REMARK] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_BOOKING_ID + "1 "
            + PREFIX_DATE + "2025-04-01 9:00 PM "
            + PREFIX_PAX + "4 "
            + PREFIX_REMARK + "Anniversary ";

    public static final String MESSAGE_EDIT_BOOKING_SUCCESS = "Edited Booking: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_BOOKING_NOT_FOUND = "No booking with ID %1$d was found.";
    public static final String MESSAGE_PAST_BOOKING_WARNING = "Warning: Editing a booking to a past date.\n";

    private final int bookingId;
    private final HashMap<String, Object> fieldsToEdit;

    /**
     * @param bookingId ID of the booking in the filtered booking list to edit
     * @param fieldsToEdit A map containing the fields to edit and their new values
     */
    public EditBookingCommand(int bookingId, HashMap<String, Object> fieldsToEdit) {
        requireNonNull(fieldsToEdit);
        this.bookingId = bookingId;
        this.fieldsToEdit = fieldsToEdit;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Booking> bookingList = model.getAddressBook().getBookingList();
        Booking bookingToEdit = bookingList.stream()
                .filter(booking -> booking.getBookingId() == bookingId)
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_BOOKING_NOT_FOUND, bookingId)));

        // Use the instance method in Booking to update the fields
        bookingToEdit.updateFields(fieldsToEdit);
        Person bookingMaker = bookingToEdit.getBookingPerson();

        model.updateFilteredBookingList(PREDICATE_SHOW_UPCOMING_BOOKINGS);

        // Update the filtered person list to show the new booking
        model.setPerson(bookingMaker, bookingMaker);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Add warning if edited date is in the past
        LocalDateTime newDateTime = (LocalDateTime) fieldsToEdit.get("bookingDateTime");
        String warningMessage = "";

        if (newDateTime != null && newDateTime.isBefore(LocalDateTime.now())) {
            warningMessage = MESSAGE_PAST_BOOKING_WARNING;
        }
        return new CommandResult(warningMessage
                + String.format(MESSAGE_EDIT_BOOKING_SUCCESS, Messages.format(bookingToEdit)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditBookingCommand)) {
            return false;
        }

        EditBookingCommand otherCommand = (EditBookingCommand) other;
        return bookingId == otherCommand.bookingId
                && fieldsToEdit.equals(otherCommand.fieldsToEdit);
    }
}
