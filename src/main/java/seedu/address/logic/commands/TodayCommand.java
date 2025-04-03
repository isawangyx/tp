package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.booking.Booking;
import seedu.address.model.person.Person;


/**
 * Filters for all bookings that are happening today, and all persons that made bookings today.
 */
public class TodayCommand extends Command {

    public static final String COMMAND_WORD = "today";

    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Here are the bookings and related persons for today:";
    public static final String MESSAGE_NO_BOOKING = "There are no bookings today.";

    private final LocalDate dateOfInterest;

    /**
     * Creates an AddPersonCommand to add the specified {@code Person}
     */
    public TodayCommand(LocalDate date) {
        requireNonNull(date);
        dateOfInterest = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Predicate<Booking> bookingPredicate = booking -> true;
        bookingPredicate = bookingPredicate.and(
                booking -> booking.getBookingDateTime().toLocalDate().equals(dateOfInterest));

        model.updateFilteredBookingList(bookingPredicate);

        if (model.getFilteredBookingList().isEmpty()) {
            // do not touch persons list if there are no bookings
            return new CommandResult(MESSAGE_NO_BOOKING);
        }

        ObservableList<Booking> filteredBookingList = model.getFilteredBookingList();
        Predicate<Person> personPredicate = person -> true;
        personPredicate = personPredicate.and(person -> {
            for (Booking booking : filteredBookingList) {
                if (booking.getBookingPerson().equals(person)) {
                    return true;
                }
            }
            return false;
        });
        model.updateFilteredPersonList(personPredicate);

        // for each booking status upcoming completed cancelled, count the number of bookings
        // put that in success message too
        int upcomingCount = 0;
        int completedCount = 0;
        int cancelledCount = 0;
        for (Booking booking : filteredBookingList) {
            switch (booking.getStatus()) {
            case UPCOMING:
                upcomingCount++;
                break;
            case COMPLETED:
                completedCount++;
                break;
            case CANCELLED:
                cancelledCount++;
                break;
            default:
                break;
            }
        }

        // "Upcoming: X, Completed: Y, Cancelled: Z"
        String outline = "Upcoming: %d, Completed: %d, Cancelled: %d";
        String message = String.format(outline, upcomingCount, completedCount, cancelledCount);

        return new CommandResult(MESSAGE_SUCCESS + "\n" + message);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TodayCommand)) {
            return false;
        }

        TodayCommand otherCommand = (TodayCommand) other;
        return dateOfInterest.equals(otherCommand.dateOfInterest);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Date of interest", dateOfInterest)
                .toString();
    }
}
