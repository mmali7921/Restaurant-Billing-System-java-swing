# BurgerWala POS System

A fully-featured, production-ready restaurant Point of Sale (POS) system built with Java Swing and SQLite. Originally a college assignment, this project has been significantly upgraded to support multi-item cart management, receipt printing, Indian GST compilation, order history tracking, and a secure admin panel.

## Features

- **Multi-item Cart Flow**: Add multiple burgers with custom quantities to a single order.
- **Dynamic Category Menu**: Browse items filtered by 'Veg', 'Non-Veg', and 'Premium'. 
- **Receipts & Printing**: Auto-generated GST-compliant invoices with an option to print to a physical printer or save as `.txt`.
- **Order History**: Check all past orders using a robust `JTable` view, complete with real-time daily revenue tracking.
- **Admin Settings**: A PIN-protected configuration panel to customize restaurant name, GSTIN, and tagline. 
- **Persistence**: All orders and config seamlessly managed using robust SQLite integration `DatabaseManager`.
- **UI Enhancements**: Utilizes Java's 'Nimbus' Look and Feel, complete with a tabbed layout, dynamic Top Toolbar containing a live digital clock, metrics, and visually appealing component design.

## Technologies Used

- **Java Swing**: Rich graphical user interface (GUI) with tabbed panes, customized `JList` renderers, and responsive `BorderLayout` design.
- **SQLite (JDBC)**: Robust database engine for storing `orders` and `order_items` across sessions and preserving configuration logic.
- **Java Print API**: Hardware integration for issuing literal tax invoices to physical printers via standard device dialogs. 
- **Java 8+**: Core language logic for models, financial math, and callback event handling.

## Getting Started

### Prerequisites
- **Java JDK 8 or higher** installed.
- Ensure the `sqlite-jdbc` connector is set in your compiler classpath if you're not utilizing a native Java bundle that incorporates it. 

### Installation & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/muhammedali/Restaurant-Billing-System-java-swing.git
   cd Restaurant-Billing-System-java-swing
   ```

2. **Compile**:
   Navigate to the `src` folder and compile the app:
   ```bash
   cd src
   javac *.java
   ```

3. **Run**:
   ```bash
   java App
   ```

## Database Schema

The project automatically initializes `burgernama.db` using the following central schema:

```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_number TEXT NOT NULL,
    table_number INTEGER,
    cashier_name TEXT,
    order_time TEXT,
    status TEXT,
    subtotal REAL,
    cgst REAL,
    sgst REAL,
    total REAL
);

CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER,
    item_name TEXT,
    quantity INTEGER,
    item_price REAL,
    toppings TEXT,
    toppings_cost REAL,
    line_total REAL,
    FOREIGN KEY(order_id) REFERENCES orders(id)
);
```
