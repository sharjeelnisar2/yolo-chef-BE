# Yolo-Chef-BE

Welcome to the Yolo-Chef Backend project! This is a Java Spring Boot application that serves as the backend for the Yolo-Chef platform, providing APIs and services for managing and interacting with chefs and their related data.

## Getting Started

### Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java 17**: Make sure you have JDK 21 installed. You can download it from (https://www.oracle.com/pk/java/technologies/downloads/#java21).
- **MySQL**: Install MySQL and set up a database. You can download MySQL from [here](https://dev.mysql.com/downloads/workbench/).
- **Git**: Ensure you have Git installed to clone the repository. Download it from [Git's official site](https://git-scm.com/downloads).

### Cloning the Repository

1. **Clone the repository**:

    ```bash
    git clone https://github.com/sharjeelnisar2/yolo-chef-BE.git
    ```

2. **Navigate into the project directory**:

    ```bash
    cd yolo-chef-BE
    ```

### Setting Up the Database

1. **Create a MySQL database**:

    ```sql
    CREATE DATABASE yolo_chef;
    ```

2. **Update the `application.properties` file** with your MySQL credentials:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/yolo_chef
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
    ```

### Installing Dependencies

Run the following command to install the project's dependencies:

```bash
mvn clean install
