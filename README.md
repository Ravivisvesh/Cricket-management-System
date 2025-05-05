
# 🏏 Cricket Manager (Java Swing + MySQL)

This is a simple desktop application built using **Java Swing** and **MySQL** to manage cricket player data. The app supports full CRUD (Create, Read, Update, Delete) operations with a clean UI and database connectivity.

---

## 📦 Features

- ➕ Insert new players
- 📋 Display all players
- 🔁 Update player details
- 🗑️ Delete a player by name
- 🧼 Clear form fields

---

## 🛠️ Tech Stack

- 💻 Java (Swing for GUI)
- 🗃️ MySQL (JDBC for database operations)
- 🎨 Aesthetic user interface using `Font`, `Color`, and layout managers

---

## 🧾 Database Setup

1. Import the SQL file: [`cricketdb.sql`](./cricketdb.sql)
2. It will create a `cricketdb` database and a `players` table:

```
CREATE DATABASE cricketdb;

USE cricketdb;

CREATE TABLE players (
    name VARCHAR(100) PRIMARY KEY,
    country VARCHAR(50),
    role VARCHAR(50),
    runs INT
);
```

---

## 🚀 How to Run

1. Ensure MySQL is running and the database is set up.
2. Open the Java file `CricketManager.java` in your IDE.
3. Update the database credentials in the `connect()` method:
   ```java
   DriverManager.getConnection("jdbc:mysql://localhost:3306/cricketdb", "root", "your_password");
   ```
4. Run the program! 🎉

## 🙌 Author

Made By Ravi visvesh

