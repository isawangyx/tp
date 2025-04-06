package seedu.address.logic;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.booking.Booking;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ")
                .append(String.join(", ", person.getTags().stream().map(Tag::toString).toList()))
                .append("; Member: ")
                .append(person.getMemberStatus() ? "Yes" : "No");
        return builder.toString();
    }

    /**
     * Formats the {@code booking} for display to the user.
     */
    public static String format(Booking booking) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Booking Date: ")
                .append(booking.getBookingDateTime().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a")))
                .append("; Booking Number: ")
                .append(booking.getBookingPerson().getPhone())
                .append("; Pax: ")
                .append(booking.getPax())
                .append("; Remark: ")
                .append(booking.getRemarks());

        return builder.toString();
    }
}
