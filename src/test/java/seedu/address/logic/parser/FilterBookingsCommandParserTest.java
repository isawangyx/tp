package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterBookingsCommand;
import seedu.address.model.booking.Status;
import seedu.address.model.person.Phone;

public class FilterBookingsCommandParserTest {

    private final FilterBookingsCommandParser parser = new FilterBookingsCommandParser();

    @Test
    public void parse_validPhoneOnly_success() throws Exception {
        String input = " p/91234567";
        FilterBookingsCommand expected = new FilterBookingsCommand(new Phone("91234567"), null, null);
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_validDateOnly_success() throws Exception {
        String input = " d/2025-12-31";
        LocalDateTime expectedDate = LocalDateTime.of(2025, 12, 31, 0, 0);
        FilterBookingsCommand expected = new FilterBookingsCommand(null, expectedDate, null);
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_validStatusOnly_success() throws Exception {
        String input = " s/completed";
        FilterBookingsCommand expected = new FilterBookingsCommand(null, null, Status.COMPLETED);
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_validAllFields_success() throws Exception {
        String input = " p/91234567 d/2025-12-31 s/UPCOMING";
        LocalDateTime expectedDate = LocalDateTime.of(2025, 12, 31, 0, 0);
        FilterBookingsCommand expected = new FilterBookingsCommand(new Phone("91234567"),
                expectedDate, Status.UPCOMING);
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_missingAllFields_failure() {
        String input = "";
        assertParseFailure(parser, input,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterBookingsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPhone_failure() {
        String input = " p/12";
        assertParseFailure(parser, input, "Invalid phone number format.");
    }

    @Test
    public void parse_invalidDate_failure() {
        String input = " d/31-12-2025";
        assertParseFailure(parser, input, "Invalid date format.");
    }

    @Test
    public void parse_invalidStatus_failure() {
        String input = " s/finished";
        assertParseFailure(parser, input, "Invalid status. Use UPCOMING, COMPLETED, or CANCELLED.");
    }
}

