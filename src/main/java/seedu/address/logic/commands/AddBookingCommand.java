package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.booking.Booking;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;


/**
 * Adds a person to the address book.
 */
public class AddBookingCommand extends Command {

    public static final String COMMAND_WORD = "badd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a booking "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_PAX + "PAX "
            + "[" + PREFIX_REMARK + "REMARK] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2021-10-01 3:00 PM "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_PAX + "5 "
            + PREFIX_REMARK + "Birthday Celebration ";

    public static final String MESSAGE_SUCCESS = "New booking added: \n%1$s";
    public static final String MESSAGE_INVALID_PERSON = "No person with the given phone number exists";
    public static final String MESSAGE_PAST_BOOKING_WARNING = "Warning: You are adding a booking for a past date!";

    // Store these instead of a whole Booking object
    // because a booking should only be created after
    // the phone number is verified.
    // Phone number will be verified upon execute, in the context of the model
    private final Phone phoneToAdd;
    private final LocalDateTime bookingDateToAdd;
    private final String remarkToAdd;
    private final int paxToAdd;

    /**
     * Creates an AddPersonCommand to add the specified {@code Booking}
     */
    public AddBookingCommand(Phone phone, LocalDateTime bookingDate, String remark, int pax) {
        requireNonNull(phone);
        requireNonNull(bookingDate);
        phoneToAdd = phone;
        bookingDateToAdd = bookingDate;
        remarkToAdd = remark;
        paxToAdd = pax;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        AddressBook addressBook = (AddressBook) model.getAddressBook();

        Person bookingMaker = null;
        for (Person person : addressBook.getPersonList()) {
            if (person.getPhone().equals(phoneToAdd)) {
                bookingMaker = person;
            }
        }
        if (bookingMaker == null) {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }

        Booking toAdd = new Booking(bookingMaker, bookingDateToAdd, remarkToAdd, paxToAdd);

        // Add booking to bookingMaker's bookings set
        bookingMaker.addBookingID(toAdd.getBookingId());

        // Add booking to AddressBook booking list for debugging
        addressBook.addBooking(toAdd);

        // Update the filtered person list to show the new booking
        model.setPerson(bookingMaker, bookingMaker);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (bookingDateToAdd.isBefore(LocalDateTime.now())) {
            return new CommandResult(
                    MESSAGE_PAST_BOOKING_WARNING + "\n" + String.format(MESSAGE_SUCCESS, Messages.format(toAdd))
            );
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddBookingCommand)) {
            return false;
        }

        AddBookingCommand otherAddBookingCommand = (AddBookingCommand) other;
        return phoneToAdd.equals(otherAddBookingCommand.phoneToAdd)
                && bookingDateToAdd.equals(otherAddBookingCommand.bookingDateToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("phone", phoneToAdd)
                .add("bookingDate", bookingDateToAdd)
                .toString();
    }
}
