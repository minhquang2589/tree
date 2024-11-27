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
                return null;
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

        //Miền
        String createRegionTable = """
                CREATE TABLE IF NOT EXISTS regions (
                       region_id INT AUTO_INCREMENT PRIMARY KEY,
                       region_name VARCHAR(255) NOT NULL
                   );
                """;

        //Tỉnh
        String createProvincesTable = """
                CREATE TABLE IF NOT EXISTS provinces (
                      province_id INT AUTO_INCREMENT PRIMARY KEY,
                      province_name VARCHAR(255) NOT NULL,
                      region_id INT,
                      FOREIGN KEY (region_id) REFERENCES regions(region_id)
                  );
                """;

        //Thành phố
        String createDistrictsTable = """
                CREATE TABLE IF NOT EXISTS districts (
                      district_id INT AUTO_INCREMENT PRIMARY KEY,
                      district_name VARCHAR(255) NOT NULL,
                      province_id INT,
                      FOREIGN KEY (province_id) REFERENCES provinces(province_id)
                  );
                """;

        //Xã, Phường
        String createWardsTable = """
                CREATE TABLE IF NOT EXISTS wards (
                     ward_id INT AUTO_INCREMENT PRIMARY KEY,
                     ward_name VARCHAR(255) NOT NULL,
                     district_id INT,
                     FOREIGN KEY (district_id) REFERENCES districts(district_id)
                 );
                """;

        //Cửa hàng
        String createStoreTable = """
                    CREATE TABLE IF NOT EXISTS stores (
                        store_id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        phone VARCHAR(15) UNIQUE NOT NULL,
                        region_id INT,
                        province_id INT,
                        district_id INT,
                        ward_id INT,
                        start_date DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        end_date DATE,
                        image VARCHAR(255),
                        status VARCHAR(255),
                        FOREIGN KEY (region_id) REFERENCES regions(region_id),
                        FOREIGN KEY (province_id) REFERENCES provinces(province_id),
                        FOREIGN KEY (district_id) REFERENCES districts(district_id),
                        FOREIGN KEY (ward_id) REFERENCES wards(ward_id)
                    );
                """;

        //User
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    store_id INT,
                    name VARCHAR(100) NOT NULL,
                    phone VARCHAR(15) UNIQUE NOT NULL,
                    address VARCHAR(1000),
                    password VARCHAR(255) NOT NULL,
                    gender ENUM('male', 'female', 'other') NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    birthday DATE,
                    image VARCHAR(255),
                    role ENUM('admin', 'user', 'moderator','supervisor') DEFAULT 'user',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;

        //Sản phẩm
        String createPlantsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    product_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    is_new TINYINT(1) DEFAULT 0,
                    description TEXT
                );
                """;

        // ảnh sản phẩm
        String createImagesTable = """
                CREATE TABLE IF NOT EXISTS images (
                    image_id INT AUTO_INCREMENT PRIMARY KEY,
                    image_path VARCHAR(400) NOT NULL,
                    product_id INT NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
                );
                """;

        //phân loại
        String createCategoriesTable = """
                CREATE TABLE IF NOT EXISTS categories (
                    category_id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    description TEXT
                );
                """;

        //Size
        String createSizesTable = """
                CREATE TABLE IF NOT EXISTS sizes (
                    size_id INT AUTO_INCREMENT PRIMARY KEY,
                    size VARCHAR(100) NOT NULL,
                    description TEXT
                );
                """;

        //Discount
        String createDiscountsTable = """
                CREATE TABLE IF NOT EXISTS discounts (
                    discount_id INT AUTO_INCREMENT PRIMARY KEY,
                    discount_percentage DECIMAL(5, 2) NOT NULL,
                    quantity INT NOT NULL,
                    remaining INT,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                );
                """;

        //Vouchers
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

        //Kho tổng
        String createWarehouseTable = """
                CREATE TABLE IF NOT EXISTS warehouse (
                    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
                    product_id INT NOT NULL,
                    size_id INT NOT NULL,
                    category_id INT NOT NULL,
                    quantity INT NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    barcode VARCHAR(255) UNIQUE NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                    FOREIGN KEY (size_id) REFERENCES sizes(size_id) ON DELETE CASCADE,
                    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
                );
                """;

        //kho của cửa hàng
        String createInventoryTable = """
                CREATE TABLE IF NOT EXISTS inventory (
                    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
                    warehouse_id INT NOT NULL,
                    quantity INT NOT NULL,
                    discount_id INT,
                    store_id INT NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE,
                    FOREIGN KEY (discount_id) REFERENCES discounts(discount_id) ON DELETE CASCADE,
                    FOREIGN KEY (store_id) REFERENCES stores(store_id) ON DELETE CASCADE
                );
                """;

        //bảng trung tâm
        String createVariantTable = """
                CREATE TABLE IF NOT EXISTS variants (
                    variant_id INT AUTO_INCREMENT PRIMARY KEY,
                    warehouse_id INT NOT NULL,
                    inventory_id INT NOT NULL,
                    region_id INT,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE,
                    FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id) ON DELETE CASCADE,
                    FOREIGN KEY (region_id) REFERENCES regions(region_id) ON DELETE CASCADE
                );
                """;
        // sản phẩm trả lại
        String createReturnProductsTable = """
                CREATE TABLE IF NOT EXISTS return_products (
                    return_product_id INT AUTO_INCREMENT PRIMARY KEY,
                    store_id INT NOT NULL,
                    return_quantity INT NOT NULL,
                    inventory_id INT NOT NULL,
                    return_reason VARCHAR(2000),
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (store_id) REFERENCES stores(store_id) ON DELETE CASCADE,
                    FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id) ON DELETE CASCADE
                );
                """;

        //thông tin vận chuyển sản phẩm cho của hàng
        String createTransferTable = """
                CREATE TABLE IF NOT EXISTS transfer (
                    transfer_id INT AUTO_INCREMENT PRIMARY KEY,
                    warehouse_id INT NOT NULL,
                    store_id INT NOT NULL,
                    status VARCHAR(255),
                    order_quantity INT NOT NULL,
                    note TEXT,
                    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE,
                    FOREIGN KEY (store_id) REFERENCES stores(store_id) ON DELETE CASCADE
                );
                """;

        // bảng phân phối sản phẩm cho của hàng
        String createDistributionTable = """
                CREATE TABLE IF NOT EXISTS distributions (
                    distribution_id INT AUTO_INCREMENT PRIMARY KEY,
                    warehouse_id INT NOT NULL,
                    region_id INT,
                    province_id INT,
                    district_id INT,
                    ward_id INT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    FOREIGN KEY (region_id) REFERENCES regions(region_id),
                    FOREIGN KEY (province_id) REFERENCES provinces(province_id),
                    FOREIGN KEY (district_id) REFERENCES districts(district_id),
                    FOREIGN KEY (ward_id) REFERENCES wards(ward_id),
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE
                
                );
                """;
        // bảng quản lý sale của của hàng
        String createSalesTable = """
                CREATE TABLE IF NOT EXISTS sales (
                    sale_id INT AUTO_INCREMENT PRIMARY KEY,
                    store_id INT NOT NULL,
                    warehouse_id INT NOT NULL,
                    quantity_sold INT NOT NULL,
                    total_price DECIMAL(10, 2),
                    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (store_id) REFERENCES stores(store_id) ON DELETE CASCADE,
                    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE
                );
                """;
        // khách hàng
        String createCustomersTable = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INT AUTO_INCREMENT PRIMARY KEY,
                    total_purchase INT,
                    total_use_voucher INT,
                    total_use_discount INT,
                    email VARCHAR(100) UNIQUE NULL,
                    phone VARCHAR(15) UNIQUE NULL,
                    address VARCHAR(255),
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                
                );
                """;
        // phương thức thanh toán
        String createPaymentMethodsTable = """
                CREATE TABLE IF NOT EXISTS payment_methods (
                    payment_method_id INT AUTO_INCREMENT PRIMARY KEY,
                    method_name VARCHAR(100) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """;
        // chi tiết order
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
        // sản phẩm order
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

            statement.execute(createRegionTable);
            System.out.println("Table 'regions' created successfully.");

            statement.execute(createProvincesTable);
            System.out.println("Table 'provinces' created successfully.");

            statement.execute(createDistrictsTable);
            System.out.println("Table 'districts' created successfully.");

            statement.execute(createWardsTable);
            System.out.println("Table 'wards' created successfully.");

            statement.execute(createUsersTable);
            System.out.println("Table 'users' created successfully.");

            statement.execute(createStoreTable);
            System.out.println("Table 'stores' created successfully.");

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

            statement.execute(createInventoryTable);
            System.out.println("Table 'inventory' created successfully.");

            statement.execute(createTransferTable);
            System.out.println("Table 'transfer' created successfully.");

            statement.execute(createDistributionTable);
            System.out.println("Table 'distributions' created successfully.");

            statement.execute(createVariantTable);
            System.out.println("Table 'variants' created successfully.");

            statement.execute(createSalesTable);
            System.out.println("Table 'sales' created successfully.");

            statement.execute(createCustomersTable);
            System.out.println("Table 'customers' created successfully.");

            statement.execute(createPaymentMethodsTable);
            System.out.println("Table 'payment methods' created successfully.");

            statement.execute(createOrderTable);
            System.out.println("Table 'orders' created successfully.");

            statement.execute(createReturnProductsTable);
            System.out.println("Table 'return products' created successfully.");

            statement.execute(createOrderItemsTable);
            System.out.println("Table 'order_items' created successfully.");
        }
    }


}
