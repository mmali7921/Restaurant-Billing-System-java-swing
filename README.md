# McDonald's Burger Billing System

A simple burger ordering and billing system using Java Swing and SQLite. This application allows users to select a burger, customize it with toppings, and generate a bill. The system also saves the billing details in a local SQLite database.

## Features

- **Burger Selection**: Users can choose from a list of available burgers.
- **Topping Customization**: Users can select multiple toppings for their burgers.
- **Checkout**: After selecting the burger and toppings, users can proceed to checkout and view the total.
- **Bill Generation**: The bill, including the total price and selected toppings, is displayed and saved to an SQLite database.
- **Clear Form**: Users can reset the form to start a new order.
- **SQLite Integration**: Bills are stored in an SQLite database for persistence.

## Technologies Used

- **Java Swing**: Used for building the graphical user interface (GUI).
- **SQLite**: Used for storing and retrieving billing information.
- **Java**: Core language for application logic and database interaction.

## Getting Started

To run this project locally, follow the steps below:

### Prerequisites

- **Java 8 or higher**: Ensure that you have Java installed on your system.
- **SQLite**: The project uses SQLite for database operations. You do not need to install SQLite manually, as the database is created automatically.

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/yourusername/burger-billing-system.git
    cd burger-billing-system
    ```

2. **Set up the project in your IDE** (e.g., IntelliJ IDEA, Eclipse).

3. **Build and run the project**: You can now run the project directly from your IDE. The application should open with a GUI where you can select burgers, toppings, and view the generated bill.

## Usage

1. **Select a Burger**: From the available list, choose a burger.
2. **Select Toppings**: Customize your burger by selecting the desired toppings.
3. **Checkout**: After making the selections, click on "Checkout" to view the total and save the bill.
4. **Print/Save Bill**: The system will save the bill in the SQLite database for later reference.

## Database Schema

The project uses a SQLite database to store billing information. The table schema is as follows:

```sql
CREATE TABLE IF NOT EXISTS bills (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    burger_name TEXT,
    toppings TEXT,
    price REAL,
    net_total REAL
);


## Installation

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/yourusername/restaurant-billing-system.git
