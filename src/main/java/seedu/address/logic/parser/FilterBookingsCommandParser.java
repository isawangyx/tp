package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FilterBookingsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.booking.Status;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new {@code FilterCommand} object.
 */
public class FilterBookingsCommandParser implements Parser<FilterBookingsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code FilterCommand}
     * and returns a {@code FilterCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public FilterBookingsCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PHONE, PREFIX_DATE, PREFIX_STATUS);
        Phone phoneNumber = null;
        LocalDateTime bookingDate = null;
        Status status = null;

        if (argMultimap.getValue(PREFIX_PHONE).isEmpty() && argMultimap.getValue(PREFIX_DATE).isEmpty()
                && argMultimap.getValue(PREFIX_STATUS).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FilterBookingsCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            try {
                phoneNumber = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            } catch (IllegalValueException ive) {
                throw new ParseException("Invalid phone number format.", ive);
            }
        }

        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            try {
                bookingDate = ParserUtil.parseDateOnly(argMultimap.getValue(PREFIX_DATE).get());
            } catch (IllegalValueException ive) {
                throw new ParseException("Invalid date format.", ive);
            }
        }

        System.out.println("STATUS VALUE: " + argMultimap.getValue(PREFIX_STATUS));
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            String statusStr = argMultimap.getValue(PREFIX_STATUS).get().toUpperCase();
            try {
                status = Status.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                throw new ParseException("Invalid status. Use UPCOMING, COMPLETED, or CANCELLED.");
            }
        }

        return new FilterBookingsCommand(phoneNumber, bookingDate, status);
    }
}
