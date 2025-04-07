---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# KrustyKrab Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the [AddressBook-Level3](https://github.com/se-edu/addressbook-level3) project by the [SE-EDU initiative](https://se-education.org/). KrustyKrab uses the following libraries: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `pdelete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `BookingListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Booking` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("pdelete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `pdelete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Tracking Bookings

#### Implementation to Track Bookings

The `Booking` class is a class under the Model component. It is used to represent a booking made by a person. The class contains the following fields:
* `bookingId` - a unique identifier for the booking.
* `bookingPerson` - the person who made the booking.
* `bookingDateTime` - the date and time of the booking.
* `bookingMadeDateTime` - the date and time at which the booking _was made_.
* `status` - the status of the booking (e.g., Upcoming, Completed, Cancelled).
* `pax` - the number of people for the booking.
* `remark` - any remarks made by the customer.

Persons and bookings have a one-to-many relationship, where a person can have multiple bookings. 
As such:
- The `Booking` class contains a reference to the `Person` class, which represents the person who made the booking. 
- Persons have a `bookingIds` field that contains a list of booking IDs linked to the booking's `bookingId` field, which are unique identifiers for each booking made by the person.

Bookings are stored in the `UniqueBookingList` class, which is a list of unique bookings utilising a HashSet for quick retrieval of `Booking` Objects by `BookingId`, and it ensures that no two bookings with the same booking ID exist in the list.
The `UniqueBookingList` class provides methods to add, delete, and search for bookings. It also provides methods to filter bookings by date and time, and to sort bookings by date and time, all separate from the list of persons represented by `UniquePersonsList`.

#### Design considerations:

**Aspect: Storing list of bookings:**

* **Utilising HashSet for Bookings:** Faster Retrieval times.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

**Aspect: Linking bookings to person:**

* **Utilising list of integers for Ids:** Easier to store in memory.
    * Pros: `Person` class occupies less space in memory.
            Easier to save to JSON file.
    * Cons: More steps required to access bookings under a given person.


### Storage of Persons and Bookings

#### Implementation of Storage

The storage component is responsible for saving and loading the address book data and user preferences. It uses the Jackson library to convert Java objects to JSON format and vice versa. 
The implementation of Storage is based off AddressBook-Level3, with the following changes:
* The `AddressBookStorage` interface has been modified to include methods for saving and loading bookings.
* The `JsonAddressBookStorage` class has been modified to include methods for saving and loading bookings.
* The `JsonAdaptedBooking` class has been added to convert `Booking` objects to and from JSON format.
* The `JsonAdaptedPerson` class has been modified to include a list of booking IDs for each person.
* The `JsonSerializableAddressBook` class has been modified to include a list of bookings.

Storage is updated at every change to the address book, and the `StorageManager` class is responsible for managing the storage of both the address book and user preferences. The `StorageManager` class implements the `Storage` interface and uses the `JsonAddressBookStorage` and `JsonUserPrefStorage` classes to read and write data.

Given below is a class diagram of the `Storage` component:
<puml src="diagrams/StorageClassDiagram.puml" width="550" />

#### Design considerations:

**Aspect: Storing list of bookings:**

* **New field for array of Bookings:** Easier to understand and debug.
    * Pros: JSON format is simple and intuitive.
    * Cons: Requires inputs in fields to be correct.

**Aspect: Linking bookings to person:**

* **Utilising list of integers for Booking IDs:** Less space to store in the JSON file.
    * Pros: Ensures every `Booking` object is tied to a `Person` object by its ID.
    * Cons: Fails to load JSON file if incorrectly set up.


### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `pdelete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `padd` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `pdelete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

The proposed data archiving feature is designed to allow users to archive old bookings, which can help in managing the address book more efficiently.
_Implementation to be discussed for future revisions._

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Restaurant staff with a significant number of bookings
* needs to efficiently manage customer bookings, including status, timing, contact details, and past records
* wants to personalize services by recognizing regular customers
* prefers desktop apps for quick searching and retrieval of booking details
* can type fast
* is reasonably comfortable using CLI apps

**Value proposition**: 

* Manage customers' information and bookings faster than a typical mouse/GUI driven app
* Accommodate customers who need to reschedule, making for a flexible booking tool
* Track customers' visit history and preferences for personalized services
* Summarise daily bookings to streamline shift preparation and optimize resource allocation


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​         | I want to …​                                                 | So that I can…​                                            |
|----------|----------------|--------------------------------------------------------------|------------------------------------------------------------|
| `* * *`  | restaurant staff  | add a new booking with customer details                      | keep track of upcoming reservations                        |
| `* * *`  | restaurant staff  | delete a booking                                             | remove cancellations or incorrect entries                  |
| `* * *`  | restaurant staff  | view a list of all upcoming bookings                         | quickly see my schedule                                    |
| `* * *`  | restaurant staff  | mark a booking as completed                                  | keep track of which customers have visited                 |
| `* * *`  | restaurant staff  | add notes to a customer profile                              | remember preferences like seating choices, allergies, etc. |
| `* * *`  | restaurant staff  | search for a booking using a customer’s name or phone number | quickly find their reservation details                     |
| `* * *`  | restaurant staff  | save a new customer’s contact information                    | quickly find their details for future bookings             |
| `* * *`  | restaurant staff  | edit a customer’s contact details                            | update incorrect or outdated information                   |
| `* * *`  | restaurant staff  | delete customer's contact details                            | maintain an updated list                                   |
| `* * *`  | restaurant staff  | store data offline                                           | keep all information for future use and tracking           |
| `* * *`  | restaurant staff  | view all upcoming bookings                                   | prepare for each day                                       |
| `* *`    | restaurant staff  | delete all completed bookings                                | clear unnecessary backlogs                                 |
| `* *`    | restaurant staff  | edit an existing booking                                     | update details when a customer changes their reservation   |
| `* *`    | restaurant staff  | sort bookings by date or time                                | find what I need quickly                                   |
| `* *`    | restaurant staff  | filter bookings by date                                      | see only the relevant reservations                         |
| `* * `   | restaurant staff  | view a customer’s past booking history                       | provide a more personalized experience                     |
| `* *`    | restaurant staff  | view a summary of today’s bookings on the homepage           | see important details at a glance                          |
| `* *`    | restaurant staff  | tag members                                                  | provide them with special perks or greetings               |
| `*`      | restaurant staff  | validate emails                                              | ensure they are in the correct input format                |

### Use cases

(For all use cases below, the **System** is `KrustyKrab` and the **Actor** is the `user`, unless specified otherwise)

---

### **Use case: Add a person**

**MSS**
1. User requests to add a new person by entering their details (name, phone, email, address, membership status and optional tags).
2. KrustyKrab validates the input.
3. KrustyKrab saves the new person to the address book.
4. KrustyKrab displays a confirmation message:  
   *"New person added: [person details]"*

Use case ends.

**Extensions**
- **1a.** Invalid input (e.g., empty name, invalid email format, invalid phone number).
    - 1a1. KrustyKrab shows error message.
    - 1a2. Use case resumes at step 1.

- **1b.** Person with the same phone number already exists.
    - 1b1. KrustyKrab alerts the user that the person already exists.
    - 1b2. Use case ends.

---

### **Use case: Edit a person**

**MSS**
1. User requests to edit an existing person by specifying their index and providing new details (name, email, address, membership status, and optional tags).
2. KrustyKrab validates the input (ensuring no empty fields, valid email format).
3. KrustyKrab saves the new person to the address book and notifies the user.

Use case ends.

**Extensions**
- **1a.** Invalid input (e.g., empty name, invalid email format).
    - 1a1. KrustyKrab shows error message.
    - 1a2. Use case resumes at step 1.
- **1b.** No person found with the provided index.
    - 1b1. KrustyKrab shows error message.
    - 1b2. Use case resumes at step 1.
- **1c.** Attempting to edit the phone number.
    - 1b1. KrustyKrab shows error message.
    - 1b2. Use case ends.

---

### **Use case: Delete a person**

**MSS**
1. User requests to delete a person.
2. KrustyKrab deletes the person from the list and notifies the user.

Use case ends.

**Extensions**
- **1a.** The given index is invalid (out of range or non-positive integer).
    - 1a1. KrustyKrab shows an error message.
    - 1a2. Use case resumes at step 1.

---

### **Use case: View a list of all persons**

**MSS**
1. User requests to view a list of all persons.
2. KrustyKrab displays the list of persons to the user.

Use case ends.

---

### **Use case: Find a person**

**MSS**
1. User requests to find a person, providing one or more search keywords.
2. KrustyKrab displays the students that fit the search term.
3. User scrolls through the displayed list to find the person they are looking for.

Use case ends.

**Extensions**
- **1a.** Invalid input (e.g., no keywords).
    - 1a1. KrustyKrab shows error message.
    - 1a2. Use case resumes at step 1.
- **1b.** No matches found for the search term.
    - 1b1. KrustyKrab shows “0 persons listed!”
    - 1b2. Use case ends.

---

### **Use case: Add a booking**

**MSS**
1. User requests to add a new booking by providing phone number, date/time, pax and optional remark.
2. KrustyKrab checks if a person with the given phone number exists.
    - If **no**, KrustyKrab shows: *"No person with the given phone number exists."*  
      Use case ends.
    - If **yes**, continue to step 3.
3. KrustyKrab validates the booking inputs.
4. KrustyKrab saves the booking under the person’s record.
5. KrustyKrab displays confirmation:  
   *"New booking added: [booking details]"*

Use case ends.

**Extensions**
- **2a.** Person not found.
    - 2a1. KrustyKrab displays error.
    - 2a2. Use case ends.

- **3a.** Invalid booking data (e.g., negative pax, wrong date format).
    - 3a1. KrustyKrab shows relevant error message.
    - 3a2. Use case resumes at step 1.

---

### **Use case: Edit a booking**

**MSS**
1. User requests to edit an existing booking by specifying their index and providing new details.
2. KrustyKrab validates the input.
3. KrustyKrab saves the new booking to the address book and notifies the user.

Use case ends.

**Extensions**
- **1a.** Invalid input (e.g., empty booking id, negative pax, wrong date format)
    - 1a1. KrustyKrab shows error message.
    - 1a2. Use case resumes at step 1.
- **1b.** No booking found with the provided index.
    - 1b1. KrustyKrab shows error message.
    - 1b2. Use case ends.


---

### **Use case: Delete a booking**

**MSS**
1. User requests to delete a booking by specifying their index.
2. KrustyKrab deletes the booking.
3. KrustyKrab displays a confirmation message.

Use case ends.

**Extensions**
- **1a.** The given index is invalid (out of range or non-positive integer).
    - 1a1. KrustyKrab shows an error message.
    - 1a2. Use case resumes at step 1.

---

### **Use case: Mark a booking**

**MSS**
1. User requests to mark a specific booking as upcoming, completed or cancelled with index.
2. KrustyKrab marks the booking with the specified status.
3. KrustyKrab displays a confirmation message.

Use case ends.

**Extensions**
- **1a.** The given index is invalid (out of range or non-positive integer).
    - 1a1. KrustyKrab shows an error message.
    - 1a2. Use case resumes at step 1.

---

### **Use case: View a list of bookings**

**MSS**
1. User requests to view a list of all bookings with status "UPCOMING".
2. KrustyKrab displays a list of bookings with status "UPCOMING".

Use case ends.

**Extensions**
- **1a.** User provides the `/all` flag.
    - 1a1. KrustyKrab displays all bookings, including completed and cancelled ones.
    - 1a2. Use case ends.

- **1b.** There are no bookings with status "UPCOMING".
    - 1b1. KrustyKrab displays: "There are no upcoming bookings."
    - 1b2. Use case ends.

---

### **Use case: Filter bookings**

**MSS**
1. User requests to filter bookings by providing a phone number, booking date or booking status.
2. KrustyKrab applies the filtering criteria (phone number and/or booking date and/or booking status).
3. KrustyKrab retrieves all bookings associated with the filter.
4. KrustyKrab displays the filtered list of bookings.

Use case ends.

**Extensions**
- **1a.** Invalid input (e.g., empty keyword, invalid phone number format, incorrect date format, invalid status).
    - 1a1. KrustyKrab shows error message.
    - 1a2. Use case resumes at step 1.
- **2a.** No bookings match the filtering criteria.
    - 2a1. KrustyKrab notifies the user that no bookings were found matching the filter.
    - 2a2. Use case ends.

---

### **Use case: Summarise bookings of the day **

**MSS**
1. User requests to show all bookings for today along with a summary count of today's upcoming, completed and cancelled bookings.
2. KrustyKrab displays  
- A list of all bookings for the current date.
- A summary count of each booking status.

Use case ends.

**Extensions**
- **1a.** No bookings found for today.
    - 1a1. KrustyKrab shows a message: "There are no bookings today."
    - 1a2. Use case ends.
---

### **Use case: Clear completed and cancelled bookings**

**MSS**
1. User requests to clear all completed and cancelled bookings.
2. KrustyKrab deletes all bookings that are marked as completed or cancelled.
3. KrustyKrab displays a confirmation message.

Use case ends.

---

### **Use case: Clear all persons and bookings**

**MSS**
1. User requests to clear the entire database (persons list and bookings list).
2. KrustyKrab clears the database. 
3. KrustyKrab displays a confirmation message.

Use case ends.

---

### **Use case: Exit**

**MSS**
1. User requests to exit the application.
2. KrustyKrab displays a confirmation message and closes the application.

Use case ends.


---


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not KrustyKrab admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The codebase should be easily extendable without major changes to the UML of the application.
5.  Any command execution should be completed within a second of user input.

*{More to be added}*

### Glossary

* **CLI**: Stands for Command-Line Interface, a text-based interface where you can input commands that interact with the program. 
* **GUI**: Stands for Graphical User Interface, a user interface where you interact with a program through graphical icons and visual indicators.
* **Mainstream OS**: Windows, Linux, Unix, macOS
* **Private Contact Detail**: A contact detail that is not meant to be shared with others
* **Booking**: A reservation entry associated with a contact, storing details such as date, time, and optional tags
* **Tag**: A custom label assigned to a contact or booking to help with categorization and filtering
* **Index**: A 1-based position number used to reference a booking in a contact’s list
--------------------------------------------------------------------------------------------------------------------
## **Appendix: Planned Enhancements**

1. **Enhance Booking Conflict Detection**: Implement a feature to ensure that upcoming bookings are not overlapped with each other, considering factors like booking type, duration, and person availability. This will prevent double-booking and improve user experience by making sure only valid booking slots are available. 
2. **Add Notification KrustyKrab for Upcoming Bookings**: Introduce a notification KrustyKrab to alert users of their upcoming bookings. This enhancement will include customizable notification preferences, allowing users to choose when they want to be reminded (e.g., 24 hours before, 1 hour before, etc.).
3. **Improve Reporting Capabilities**: Expand the reporting features to allow users to generate detailed reports of booking history, including the number of bookings made, booking frequency, and utilization rates. This will help users track booking patterns and identify potential areas for optimization.

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Effort**

**Team size:**: 5
**Difficulty level:**
Our project was significantly more complex compared to AB3. We introduced various new commands to support additional functionalities. The `Person` objects now include more fields, with certain fields ('BookingIds') requiring syncing to the `Booking` objects. The UI has also become more complicated, with the introduction of two split panes that reflect `Person` and `Booking` data simultaneously. 

**Challenges faced:**
1. Complexity of Features:
   Implementing features such as Person and Booking management required a deep understanding of the existing codebase and careful planning to ensure seamless integration of various functionalities like  filtering, adding, editing, and syncing Bookings with Persons.
2. User Interface:
   Ensuring the UI remains intuitive while accommodating new features was challenging.

**Effort required:**
1. Planning and Design: 20 hours were spent on planning and designing the Person and Booking features and their integration with the existing system.
2. Implementation: 85 hours were dedicated to coding, debugging, and refining the new features for managing Person and Booking entities. 
3. Testing: 35 hours were spent on writing and executing test cases to ensure the reliability of the new features.
4. Documentation: 10 hours were used to update the Developer Guide and User Guide to reflect the new features and changes.

**Achievements:**
We have extended AB3 with multiple new features and a better UI, to make the app more specialised and suitable for restaurant staff.

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder
   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `plist` command. Multiple persons in the list.

   2. Test case: `pdelete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `pdelete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `pdelete`, `pdelete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Normal saving and loading

   1. After some edits (i.e. not just the dummy data), `exit` or simply close the app.
   2. Re-launch the app, and confirm that the same data is there, using commands like `plist` and `blist /all`.

1. Dealing with missing/corrupted data files
   1. Missing: An empty address book will be created.
      The following will be logged as an info: "Creating a new data file " + storage.getAddressBookFilePath()
      + " populated with a sample AddressBook.".
   2. Corrupted: A DataLoadingException is thrown. An empty address book will be created.
      The following will be logged as a warning: "Data file at " + storage.getAddressBookFilePath() + " could not be loaded."
      + " Will be starting with an empty AddressBook.".
