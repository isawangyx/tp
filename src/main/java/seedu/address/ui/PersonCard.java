package seedu.address.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.booking.Booking;
import seedu.address.model.booking.Status;
import seedu.address.model.booking.UniqueBookingList;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label memberLabel;
    @FXML
    private Label bookingCount;
    @FXML
    private FlowPane tags;
    @FXML
    private VBox bookingTagPane;


    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, UniqueBookingList bookings) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        if (person.getMemberStatus()) {
            memberLabel.setText("MEMBER");
            memberLabel.setVisible(true);
            HBox.setMargin(memberLabel, new Insets(0, 0, 0, 4));
        } else {
            memberLabel.setVisible(false);
        }

        List<Booking> upcomingBookings = new ArrayList<>();
        for (Integer bookingId : person.getBookingIDs()) {
            Booking booking = bookings.getBooking(bookingId);
            if (booking != null && booking.getStatus() == Status.UPCOMING) {
                upcomingBookings.add(booking);
            }
        }
        upcomingBookings.sort(Comparator.comparing(Booking::getBookingDateTime));

        bookingCount.setText("Upcoming Bookings:  " + upcomingBookings.size());

        for (Booking booking : upcomingBookings) {
            if (booking != null && booking.getStatus() == Status.UPCOMING) {
                HBox bookingDetails = new HBox();
                bookingDetails.setSpacing(5);

                Label dateTimeLabel = new Label(formatDateTime(booking.getBookingDateTime()));
                dateTimeLabel.getStyleClass().add("yellow-tag");
                dateTimeLabel.setMinWidth(Region.USE_PREF_SIZE);
                bookingDetails.getChildren().add(dateTimeLabel);

                Label paxLabel = new Label(booking.getPax() + " pax");
                paxLabel.getStyleClass().add("purple-tag");
                paxLabel.setMinWidth(Region.USE_PREF_SIZE);
                bookingDetails.getChildren().add(paxLabel);

                if (!booking.getRemarks().isEmpty()) {
                    Label remarkLabel = new Label(booking.getRemarks());
                    remarkLabel.getStyleClass().add("green-tag");
                    bookingDetails.getChildren().add(remarkLabel);
                }

                bookingTagPane.getChildren().add(bookingDetails);
            }
        }

    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a, dd MMM yyyy");
        return dateTime.format(formatter);
    }
}
