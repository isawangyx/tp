package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    // Example: 2020-03-03 2:00 PM
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd h:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.ENGLISH);
    private static final DateTimeFormatter DATE_ONLY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    private static final int MAX_PERSONS = 500;

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }


    /**
     * Parses a {@code String dateStr} into a {@code LocalDateTime}.
     */
    public static LocalDateTime parseDateTime(String dateStr) throws ParseException {
        requireNonNull(dateStr);
        String trimmed = dateStr.trim();

        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(trimmed, FORMATTER);
            String normalized = parsedDateTime.format(FORMATTER);
            if (!normalized.equalsIgnoreCase(trimmed)) {
                throw new ParseException("Invalid date/time: " + dateStr
                        + "\nDid you mean: " + normalized + "?"
                        + "\nPlease follow the format: " + DATE_TIME_FORMAT);
            }

            return parsedDateTime;

        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format: " + dateStr
                    + "\nPlease follow the format: " + DATE_TIME_FORMAT
                    + "\nExample: 2020-03-03 2:00 PM");
        }
    }

    /**
     * Parses a {@code String dateStr} into a {@code LocalDateTime} with time set to midnight (00:00).
     * Format expected: yyyy-MM-dd (e.g., 2023-12-25)
     */
    public static LocalDateTime parseDateOnly(String dateStr) throws ParseException {
        requireNonNull(dateStr);

        try {
            // Parse the date string to LocalDate first
            java.time.LocalDate date = java.time.LocalDate.parse(dateStr, DATE_ONLY_FORMATTER);

            // Convert LocalDate to LocalDateTime with time set to midnight
            return date.atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid date format: " + dateStr
                    + "\nPlease follow the format: yyyy-MM-dd"
                    + "\nExample: 2023-12-25");
        }
    }

    /**
     * Parses a {@code String pax} into an {@code int}.
     *
     * @throws ParseException if the given {@code pax} is invalid.
     */
    public static int parsePax(String pax) throws ParseException {
        requireNonNull(pax);
        int parsedPax;
        String errorMessage = "Pax should be a positive integer between 1 and " + MAX_PERSONS
                + ". Please enter a valid number.";
        try {
            parsedPax = Integer.parseInt(pax);
        } catch (NumberFormatException e) {
            throw new ParseException(errorMessage);
        }
        if (parsedPax < 1 || parsedPax > MAX_PERSONS) {
            throw new ParseException(errorMessage);
        }
        return parsedPax;
    }

    /**
     * Parses a {@code String isMember} into a {@code boolean}.
     *
     * @throws ParseException if the given {@code isMember} is invalid.
     */
    public static boolean parseIsMember(String isMember) throws ParseException {
        isMember = isMember.toLowerCase();
        if (isMember.equals("1") || isMember.equals("yes") || isMember.equals("true")) {
            return true;
        } else if (isMember.equals("0") || isMember.equals("no") || isMember.equals("false")) {
            return false;
        } else {
            throw new ParseException("isMember should be either 1/0, yes/no, true/false.");
        }
    }
}
