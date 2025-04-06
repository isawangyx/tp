package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddBookingCommand;
import seedu.address.model.person.Phone;

public class AddBookingCommandParserTest {

    private final AddBookingCommandParser parser = new AddBookingCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String input = " d/2025-04-01 6:00 PM p/85355255 x/4 r/Birthday";
        Phone phone = new Phone("85355255");
        LocalDateTime bookingDate = LocalDateTime.of(2025, 4, 1, 18, 0);
        String remark = "Birthday";
        int pax = 4;

        AddBookingCommand expectedCommand =
                new AddBookingCommand(phone, bookingDate, remark, pax);

        assertParseSuccess(parser, input, expectedCommand);
    }

    @Test
    public void parse_missingRequiredFields_failure() {
        // Missing phone
        String inputMissingPhone = " d/2025-04-01 18:00 x/4 r/Birthday";
        assertParseFailure(parser, inputMissingPhone,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBookingCommand.MESSAGE_USAGE));

        // Missing date
        String inputMissingDate = " p/85355255 x/4 r/Birthday";
        assertParseFailure(parser, inputMissingDate,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBookingCommand.MESSAGE_USAGE));

        // Missing pax
        String inputMissingPax = " d/2025-04-01 18:00 p/85355255 r/Birthday";
        assertParseFailure(parser, inputMissingPax,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBookingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateTimeFormat_failure() {
        String input = " d/April 1st 6pm p/85355255 x/4 r/Dinner";
        assertParseFailure(parser, input, "Invalid date format: April 1st 6pm\n"
                + " Please follow the format: yyyy-MM-dd h:mm a\n"
                + " Example: 2020-03-03 2:00 PM");
    }

    @Test
    public void parse_invalidPax_failure() {
        String input = " d/2025-04-01 3:00 PM p/85355255 x/four r/Dinner";
        assertParseFailure(parser, input, "Pax should be a positive integer between 1 and 500. "
                + "Please enter a valid number.");
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        String input = " d/2025-04-01 3:00 PM d/2025-04-01 19:00 p/85355255 p/81234567 x/4";
        assertParseFailure(parser, input,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE, PREFIX_PHONE));
    }
}
