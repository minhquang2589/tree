package com.example.demo.config;
import java.sql.*;

public class MySQLConnection {
    private static Connection connection;

    public static Connection connect() {
        if (connection == null) {
            try {
                String url = "jdbc:mysql://localhost:3306";
                String user = "root";
                String password = "";
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Kết nối MySQL thành công!");
                String createDB = "CREATE DATABASE IF NOT EXISTS plants";
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(createDB);
                }

                String dbUrl = "jdbc:mysql://localhost:3306/plants";
                connection = DriverManager.getConnection(dbUrl, user, password);
                createTable();

            } catch (SQLException e) {
                System.err.println("Không thể kết nối đến MySQL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }


    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối thành công!");
            }
        } catch (SQLException e) {
            System.err.println("Không thể đóng kết nối: " + e.getMessage());
        }
    }

    public static void createTable() throws SQLException {

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    phone VARCHAR(15) UNIQUE NOT NULL,
                    address VARCHAR(1000),
                    password VARCHAR(255) NOT NULL,
                    gender ENUM('male', 'female', 'other') NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    birthday DATE,
                    image VARCHAR(255),
                    role ENUM('admin', 'user', 'moderator') DEFAULT 'user',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createPlantsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    product_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    is_new TINYINT(1) DEFAULT 0,
                    description TEXT
                );
                """;


        String createImagesTable = """
                CREATE TABLE IF NOT EXISTS images (
                    image_id INT AUTO_INCREMENT PRIMARY KEY,
                    image_path VARCHAR(400) NOT NULL,
                    product_id INT NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
                );
                """;


        String createCategoriesTable = """
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    description TEXT
                );
                """;


        String createSizesTable = """
                CREATE TABLE IF NOT EXISTS sizes (
                    size_id INT AUTO_INCREMENT PRIMARY KEY,
                    size VARCHAR(100) NOT NULL,
                    description TEXT
                );
                """;

        String createDiscountsTable = """
                CREATE TABLE IF NOT EXISTS discounts (
                    discount_id INT AUTO_INCREMENT PRIMARY KEY,
                    product_id INT NOT NULL,
                    discount_percentage DECIMAL(5, 2) NOT NULL,
                    quantity INT NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
                );
                """;

        String createVouchersTable = """
                CREATE TABLE IF NOT EXISTS vouchers (
                    voucher_id INT AUTO_INCREMENT PRIMARY KEY,
                    voucher_code VARCHAR(50) UNIQUE NOT NULL,
                    discount_percentage DECIMAL(5, 2) NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    status ENUM('active', 'inactive') DEFAULT 'active',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createWarehouseTable = """
                CREATE TABLE IF NOT EXISTS warehouse (
                    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
                    product_id INT NOT NULL,
                    size_id INT NOT NULL,
                    category_id INT NOT NULL,
                    quantity INT NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    discount_id INT,
                    barcode_image_path VARCHAR(255) UNIQUE NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                    FOREIGN KEY (size_id) REFERENCES sizes(size_id) ON DELETE CASCADE,
                    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
                    FOREIGN KEY (discount_id) REFERENCES discounts(discount_id) ON DELETE CASCADE
                
                );
                """;

        String createStoreInventoryTable = """
                CREATE TABLE IF NOT EXISTS store_inventory (
                    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    product_id INT NOT NULL,
                    size_id INT NOT NULL,
                    category_id INT NOT NULL,
                    quantity INT NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                    FOREIGN KEY (size_id) REFERENCES sizes(size_id) ON DELETE CASCADE,
                    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
                );
                """;

        String createTransferLogsTable = """
                CREATE TABLE IF NOT EXISTS transfer_logs (
                    transfer_id INT AUTO_INCREMENT PRIMARY KEY,
                    product_id INT NOT NULL,
                    from_warehouse_id INT,
                    to_user_id INT NOT NULL,
                    quantity INT NOT NULL,
                    status VARCHAR(255),
                    note TEXT,
                    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                    FOREIGN KEY (to_user_id) REFERENCES users(user_id) ON DELETE CASCADE
                );
                """;

        String createSalesTable = """
                CREATE TABLE IF NOT EXISTS sales (
                    sale_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    warehouse_id INT NOT NULL,
                    product_id INT NOT NULL,
                    quantity_sold INT NOT NULL,
                    total_price DECIMAL(10, 2),
                    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE
                );
                """;

        String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INT AUTO_INCREMENT PRIMARY KEY,
                    total_purchase INT,
                    email VARCHAR(100) UNIQUE NULL,
                    phone VARCHAR(15) UNIQUE NULL,
                    address VARCHAR(255),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createPaymentMethodsTable = """
                CREATE TABLE IF NOT EXISTS payment_methods (
                    payment_method_id INT AUTO_INCREMENT PRIMARY KEY,
                    method_name VARCHAR(100) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createOrderTable = """
                CREATE TABLE IF NOT EXISTS orders (
                    order_id INT AUTO_INCREMENT PRIMARY KEY,
                    customer_id INT,
                    voucher_id INT,
                    payment_method_id INT NOT NULL,
                    discount DECIMAL(10, 2) DEFAULT 0.00,
                    total_price DECIMAL(10, 2) NOT NULL,
                    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(255) DEFAULT 'pending',
                    order_reference VARCHAR(255),
                    note VARCHAR(2000),
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
                    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(payment_method_id) ON DELETE CASCADE,
                    FOREIGN KEY (voucher_id) REFERENCES vouchers(voucher_id) ON DELETE SET NULL
                );
                """;

        String createOrderItemsTable = """
                CREATE TABLE IF NOT EXISTS order_items (
                    order_items_id INT AUTO_INCREMENT PRIMARY KEY,
                    order_id INT NOT NULL,
                    warehouse_id INT NOT NULL,
                    quantity INT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE

                );
                """;

        Connection connection = connect();
        if (connection != null) {
            Statement statement = connection.createStatement();

            statement.execute(createUsersTable);
            System.out.println("Table 'users' created successfully.");

            statement.execute(createCategoriesTable);
            System.out.println("Table 'categories' created successfully.");

            statement.execute(createPlantsTable);
            System.out.println("Table 'products' created successfully.");

            statement.execute(createImagesTable);
            System.out.println("Table 'images' created successfully.");


            statement.execute(createSizesTable);
            System.out.println("Table 'sizes' created successfully.");

            statement.execute(createDiscountsTable);
            System.out.println("Table 'discounts' created successfully.");

            statement.execute(createVouchersTable);
            System.out.println("Table 'vouchers' created successfully.");

            statement.execute(createWarehouseTable);
            System.out.println("Table 'warehouse' created successfully.");

            statement.execute(createStoreInventoryTable);
            System.out.println("Table 'store_inventory' created successfully.");

            statement.execute(createTransferLogsTable);
            System.out.println("Table 'transfer_logs' created successfully.");

            statement.execute(createSalesTable);
            System.out.println("Table 'sales' created successfully.");

            statement.execute(createCustomersTable);
            System.out.println("Table 'customers' created successfully.");

            statement.execute(createPaymentMethodsTable);
            System.out.println("Table 'payment methods' created successfully.");

            statement.execute(createOrderTable);
            System.out.println("Table 'orders' created successfully.");

            statement.execute(createOrderItemsTable);
            System.out.println("Table 'order_items' created successfully.");
        }
    }


}
