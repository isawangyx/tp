package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.booking.Booking;
import seedu.address.model.booking.Status;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.testutil.PersonBuilder;

public class FilterBookingsCommandTest {

    @Test
    public void execute_validPhoneAndDateAndStatus_success() throws Exception {
        Person person = new PersonBuilder().withPhone("91234567").build();
        Booking booking = new Booking(person,
                LocalDateTime.of(2025, 4, 5, 18, 0), "Dinner", 4);
        booking.setBookingPerson(person);
        person.addBookingID(booking.getBookingId());

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);

        FilterBookingsCommand command = new FilterBookingsCommand(new Phone("91234567"),
                booking.getBookingDateTime(), Status.UPCOMING);

        CommandResult result = command.execute(modelStub);
        assertEquals(String.format(FilterBookingsCommand.MESSAGE_SUCCESS,
                        " for phone number 91234567 on 05 Apr 2025 with status Upcoming"),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidPhone_throwsCommandException() {
        Person person = new PersonBuilder().withPhone("91234567").build();
        Booking booking = new Booking(person, LocalDateTime.now().plusDays(1), "Dinner", 4);
        booking.setBookingPerson(person);

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);
        FilterBookingsCommand command = new FilterBookingsCommand(new Phone("99999999"),
                booking.getBookingDateTime(), Status.UPCOMING);

        assertThrows(CommandException.class,
                String.format(FilterBookingsCommand.MESSAGE_PERSON_NOT_FOUND, "99999999"), () ->
                        command.execute(modelStub));
    }

    @Test
    public void execute_noMatchingBookings_returnsNoResultsMessage() throws Exception {
        Person person = new PersonBuilder().withPhone("91234567").build();
        Booking booking = new Booking(person,
                LocalDateTime.of(2025, 4, 5, 18, 0), "Dinner", 4);
        booking.setBookingPerson(person);
        person.addBookingID(booking.getBookingId());

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);

        FilterBookingsCommand command = new FilterBookingsCommand(new Phone("91234567"),
                booking.getBookingDateTime().plusDays(1), Status.CANCELLED);

        CommandResult result = command.execute(modelStub);
        assertEquals(String.format(FilterBookingsCommand.MESSAGE_NO_BOOKINGS,
                        " for phone number 91234567 on 06 Apr 2025 with status Cancelled"),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_filterByDateOnlyWithResults_success() throws Exception {
        Person person = new PersonBuilder().build();
        Booking booking = new Booking(person, LocalDateTime.of(2025, 3, 28, 18, 0),
                "Dinner", 2);
        booking.setBookingPerson(person);

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);
        FilterBookingsCommand command = new FilterBookingsCommand(null, booking.getBookingDateTime(), null);
        CommandResult result = command.execute(modelStub);

        assertEquals(String.format(FilterBookingsCommand.MESSAGE_SUCCESS, " on 28 Mar 2025"),
                result.getFeedbackToUser());
        assertEquals(1, modelStub.getFilteredBookingList().size());
    }

    @Test
    public void execute_filterByStatusOnlyWithNoResults_showsNoBookings() throws Exception {
        Person person = new PersonBuilder().build();
        Booking booking = new Booking(person, LocalDateTime.now().plusDays(1), "Lunch", 1);
        booking.setBookingPerson(person);

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);
        FilterBookingsCommand command = new FilterBookingsCommand(null, null, Status.COMPLETED);
        CommandResult result = command.execute(modelStub);

        assertEquals(String.format(FilterBookingsCommand.MESSAGE_NO_BOOKINGS, " with status Completed"),
                result.getFeedbackToUser());
        assertEquals(0, modelStub.getFilteredBookingList().size());
    }

    @Test
    public void execute_filterByPhoneAndDateWithNoMatchingBookings_showsNoBookings() throws Exception {
        Person person = new PersonBuilder().build();
        Booking booking = new Booking(person, LocalDateTime.of(2025, 3, 28, 20, 0),
                "Supper", 2);
        booking.setBookingPerson(person);

        ModelStubWithBooking modelStub = new ModelStubWithBooking(person, booking);

        // Filtering for a date that doesn't match the booking date
        FilterBookingsCommand command = new FilterBookingsCommand(person.getPhone(),
                LocalDateTime.of(2025, 3, 29, 20, 0), null);
        CommandResult result = command.execute(modelStub);

        assertEquals(String.format(FilterBookingsCommand.MESSAGE_NO_BOOKINGS,
                        " for phone number " + person.getPhone() + " on 29 Mar 2025"),
                result.getFeedbackToUser());
        assertEquals(0, modelStub.getFilteredBookingList().size());
    }

    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {

        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {

        }

        @Override
        public void addBooking(Booking booking) {

        }

        @Override
        public ObservableList<Booking> getFilteredBookingList() {
            return null;
        }

        @Override
        public void updateFilteredBookingList(Predicate<Booking> predicate) {

        }

        @Override
        public Predicate<Person> getCurrentPersonPredicate() {
            return null;
        }

        @Override
        public Predicate<Booking> getCurrentBookingPredicate() {
            return null;
        }

        @Override
        public boolean isBookingListFiltered() {
            return false;
        }
    }

    private class ModelStubWithBooking extends ModelStub {
        private final AddressBook addressBook = new AddressBook();
        private final ObservableList<Booking> filteredBookings = FXCollections.observableArrayList();

        ModelStubWithBooking(Person person, Booking booking) {
            addressBook.addPerson(person);
            addressBook.addBooking(booking);
            filteredBookings.add(booking);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        @Override
        public ObservableList<Booking> getFilteredBookingList() {
            return filteredBookings;
        }

        @Override
        public void updateFilteredBookingList(Predicate<Booking> predicate) {
            filteredBookings.setAll(addressBook.getBookingList().filtered(predicate));
        }
    }
}
