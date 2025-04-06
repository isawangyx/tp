package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private boolean isMember; // member / public
    private Date dateJoined;
    private Set<Integer> bookingIDs = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, boolean isMember,
                  Set<Integer> bookingIDs) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.isMember = isMember;
        if (isMember) {
            this.dateJoined = new Date();
        } else {
            this.dateJoined = null;
        }
        this.bookingIDs.addAll(bookingIDs);
    }

    /**
     * Constructor overload without isMember field.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Set<Integer> bookingIDs) {
        this(name, phone, email, address, tags, false, bookingIDs);
    }

    /**
     * Constructor overload without bookings field. Loads person with no bookings.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, boolean isMember) {
        this(name, phone, email, address, tags, isMember, new HashSet<>());
    }

    /**
     * Constructor overload without bookings field and isMember field. Loads person with no bookings.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, false, new HashSet<>());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns set of bookings the person has.
     * @return set of bookings.
     */
    public Set<Integer> getBookingIDs() {
        return bookingIDs;
    }

    public boolean getMemberStatus() {
        return isMember;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Updates membership status of a person.
     */
    public boolean updateMembershipStatus(boolean isMember) {
        if (this.isMember == isMember) {
            return false;
        }

        if (isMember) {
            this.dateJoined = new Date();
        } else {
            this.dateJoined = null;
        }
        this.isMember = isMember;
        return true;
    }

    public void addBookingID(int bookingID) {
        bookingIDs.add(bookingID);
    }

    public void removeBookingID(int bookingID) {
        bookingIDs.remove(bookingID);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && bookingIDs.equals(otherPerson.bookingIDs);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, bookingIDs);
    }

    @Override
    public String toString() {

        String tagsString = tags.toString();
        if (tags.isEmpty()) {
            tagsString = "No Remarks";
        }

        // create bookings string
        String bookingIDsString = bookingIDs.toString();
        if (bookingIDs.isEmpty()) {
            bookingIDsString = "No Bookings";
        }

        String isMemberString = isMember ? "Yes" : "No";
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tagsString)
                .add("bookingIDs", bookingIDsString)
                .add("isMember", isMemberString)
                .toString();
    }
}
