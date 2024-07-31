import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Vehicle {
    private String vehicle_id;
    private String vehicle_model;
    private String vehicle_Brand;
    private double basePriceperday;
    private boolean isAvail;

    public Vehicle(String vehicle_id, String vehicle_model, String vehicle_Brand, double basePriceperday, boolean isAvail) {
        this.vehicle_id = vehicle_id;
        this.vehicle_model = vehicle_model;
        this.vehicle_Brand = vehicle_Brand;
        this.basePriceperday = basePriceperday;
        this.isAvail = isAvail;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public String getVehicle_Brand() {
        return vehicle_Brand;
    }

    public double price(int rentalDays) {
        return basePriceperday * rentalDays;
    }

    public boolean isAvail() {
        return isAvail;
    }

    public void rent() {
        this.isAvail = false;
    }

    public void returnVehicle() {
        this.isAvail = true;
    }

    @Override
    public String toString() {
        return "ID: " + vehicle_id + ", Model: " + vehicle_model + ", Brand: " + vehicle_Brand + ", Price per day: " + basePriceperday + ", Available: " + isAvail;
    }
}

class Car extends Vehicle {
    public Car(String car_id, String car_model, String car_Brand, double basePriceperday, boolean isAvail) {
        super(car_id, car_model, car_Brand, basePriceperday, isAvail);
    }
}

class Person {
    private String person_id;
    private String person_name;
    private String person_email;
    private long person_phone;

    public Person(String person_id, String person_name, String person_email, long person_phone) {
        this.person_id = person_id;
        this.person_name = person_name;
        this.person_email = person_email;
        this.person_phone = person_phone;
    }

    public String getPerson_id() {
        return person_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getPerson_email() {
        return person_email;
    }

    public long getPerson_phone() {
        return person_phone;
    }
}

class Customer extends Person {
    public Customer(String customer_id, String customer_name, String customer_email, long customer_phone) {
        super(customer_id, customer_name, customer_email, customer_phone);
    }
}

class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int days;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return vehicle.price(days);
    }
}

class RentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;
    private int customerIdCounter;
    public RentalSystem() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
        customerIdCounter = 1;
    }
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void registerCustomer() {
        Scanner sc = new Scanner(System.in);
        String customerId = "C" + customerIdCounter++;
        System.out.println("Generated customer ID: " + customerId);
        System.out.println("Enter customer name:");
        String customerName = sc.nextLine();
        System.out.println("Enter customer email:");
        String customerEmail = sc.nextLine();
        System.out.println("Enter customer phone number:");
        long customerPhone = sc.nextLong();
        sc.nextLine();
        Customer newCustomer = new Customer(customerId, customerName, customerEmail, customerPhone);
        addCustomer(newCustomer);
        System.out.println("Customer registered successfully.");
    }

    public Customer getCustomerByEmail(String email) {
        for (Customer customer : customers) {
            if (customer.getPerson_email().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    public void rentVehicle(Customer customer, Vehicle vehicle, int days) {
        if (vehicle.isAvail()) {
            double totalPrice = vehicle.price(days);
            System.out.println("Total amount to be paid: " + totalPrice);
            Scanner sc = new Scanner(System.in);
            System.out.println("Do you want to confirm the rental? (yes/no)");
            String confirmation = sc.nextLine();
            if (confirmation.equalsIgnoreCase("yes")) {
                vehicle.rent();
                vehicles.remove(vehicle);
                rentals.add(new Rental(vehicle, customer, days));
                System.out.println("Vehicle rented successfully.");
            } else {
                System.out.println("Rental not confirmed. Returning to the main menu.");
            }
        } else {
            System.out.println("Vehicle is not available for rent.");
        }
    }

    public void returnVehicle() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter customer email:");
        String email = sc.nextLine();
        Customer customer = getCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not registered. Please register first.");
            return;
        }

        List<Rental> customerRentals = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getCustomer().equals(customer)) {
                customerRentals.add(rental);
            }
        }

        if (customerRentals.isEmpty()) {
            System.out.println("No rented vehicles found for this customer.");
            return;
        }

        System.out.println("Rented vehicles:");
        for (Rental rental : customerRentals) {
            System.out.println("Vehicle ID: " + rental.getVehicle().getVehicle_id() + ", Days: " + rental.getDays() + ", Total Rent: " + rental.getTotalPrice());
        }

        System.out.println("Enter vehicle ID to return:");
        String vehicleId = sc.nextLine();
        Vehicle vehicleToReturn = null;
        Rental rentalToRemove = null;

        for (Rental rental : customerRentals) {
            if (rental.getVehicle().getVehicle_id().equals(vehicleId)) {
                vehicleToReturn = rental.getVehicle();
                rentalToRemove = rental;
                break;
            }
        }

        if (vehicleToReturn == null) {
            System.out.println("Vehicle not found in your rented list.");
            return;
        }

        rentals.remove(rentalToRemove);
        vehicleToReturn.returnVehicle();
        vehicles.add(vehicleToReturn);

        System.out.println("Vehicle has been returned.");
        System.out.println("Total amount to pay: " + rentalToRemove.getTotalPrice());
    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("**** WANT TO RENT A VEHICLE ****");
            System.out.println("1. Register Customer");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Return a Vehicle");
            System.out.println("4. Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    System.out.println("Available vehicles:");
                    for (Vehicle vehicle : vehicles) {
                        System.out.println(vehicle);
                    }
                    System.out.println("Enter customer email:");
                    String customerEmail = sc.nextLine();
                    Customer customer = getCustomerByEmail(customerEmail);
                    if (customer == null) {
                        System.out.println("Customer not registered. Please register first.");
                        break;
                    }
                    System.out.println("Enter vehicle ID to rent:");
                    String vehicleId = sc.nextLine();
                    Vehicle vehicleToRent = null;
                    for (Vehicle vehicle : vehicles) {
                        if (vehicle.getVehicle_id().equals(vehicleId)) {
                            vehicleToRent = vehicle;
                            break;
                        }
                    }
                    if (vehicleToRent == null) {
                        System.out.println("Vehicle not found.");
                        break;
                    }
                    System.out.println("Enter number of rental days:");
                    int days = sc.nextInt();
                    sc.nextLine();
                    rentVehicle(customer, vehicleToRent, days);
                    break;
                case 3:
                    returnVehicle();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
        RentalSystem rentalSystem = new RentalSystem();

        rentalSystem.addVehicle(new Car("car1", "Marti Swift", "Brand A", 1000.0, true));
        rentalSystem.addVehicle(new Car("car2", "Mahindra Scorpio", "Brand B", 2050.0, true));
        rentalSystem.addVehicle(new Car("car3", "Hyundai Creta", "Brand A", 2180.0, true));
        rentalSystem.addVehicle(new Car("car4", "Mahindra Bolero", "Brand B", 1500.0, true));
        rentalSystem.addVehicle(new Car("car5", "Model E", "Brand A", 1000.0, true));
        rentalSystem.addVehicle(new Car("car6", "Model F", "Brand B", 150.0, true));

        rentalSystem.menu();
    }
}
