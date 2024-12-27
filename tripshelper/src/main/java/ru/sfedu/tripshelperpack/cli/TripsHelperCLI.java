package ru.sfedu.tripshelperpack.cli;

import ru.sfedu.tripshelperpack.models.*;
import ru.sfedu.tripshelperpack.service.*;
import ru.sfedu.tripshelperpack.api.*;

import java.sql.SQLException;
import java.util.*;

public class TripsHelperCLI {

    private final Scanner scanner = new Scanner(System.in);

    public void start() throws SQLException {
        System.out.println("Добро пожаловать в TripsHelper CLI!");
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Работа с XML провайдером");
            System.out.println("2. Работа с CSV провайдером");
            System.out.println("3. Работа с PostgreSQL");
            System.out.println("4. Выход");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleXMLProvider();
                case "2" -> handleCSVProvider();
                case "3" -> handlePostgreSQL();
                case "4" -> {
                    System.out.println("Завершение программы. До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void handleXMLProvider() {
        System.out.print("Введите путь к XML файлу: ");
        String filePath = scanner.nextLine();

        System.out.println("Выберите модель для работы:");
        Class<?> modelClass = chooseModelClass();
        if (modelClass == null) return; // Вернуться в главное меню, если выбор не сделан

        DataProviderXML dataProviderXML = new DataProviderXML(filePath, modelClass);
        handleCRUDOperations(dataProviderXML);
    }

    private void handleCSVProvider() {
        System.out.print("Введите путь к CSV файлу: ");
        String filePath = scanner.nextLine();

        System.out.println("Выберите модель для работы:");
        Class<?> modelClass = chooseModelClass();
        if (modelClass == null) return; // Вернуться в главное меню, если выбор не сделан

        DataProviderCSV dataProviderCSV = new DataProviderCSV(filePath, modelClass);
        handleCRUDOperations(dataProviderCSV);
    }

    private void handlePostgreSQL() throws SQLException {
        System.out.println("Выберите модель для работы с PostgreSQL:");
        System.out.println("1. User");
        System.out.println("2. Trip");
        System.out.println("3. Transport");
        System.out.println("4. Attraction");
        System.out.println("5. Вернуться в главное меню");

        while (true) {
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleServiceOperations(new UserService());
                case "2" -> handleServiceOperations(new TripService());
                case "3" -> handleServiceOperations(new TransportService());
                case "4" -> handleServiceOperations(new AttractionService());
                case "5" -> {
                    System.out.println("Возвращение в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private Class<?> chooseModelClass() {
        System.out.println("1. User");
        System.out.println("2. Trip");
        System.out.println("3. Transport");
        System.out.println("4. Attraction");
        System.out.println("5. Вернуться в главное меню");

        while (true) {
            String choice = scanner.nextLine();
            return switch (choice) {
                case "1" -> User.class;
                case "2" -> Trip.class;
                case "3" -> Transport.class;
                case "4" -> Attraction.class;
                case "5" -> {
                    System.out.println("Возвращение в главное меню.");
                    yield null; // Вернуться в главное меню
                }
                default -> {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    yield null;
                }
            };
        }
    }

    private void handleCRUDOperations(Object dataProvider) {
        while (true) {
            System.out.println("\nВыберите CRUD операцию:");
            System.out.println("1. Create");
            System.out.println("2. Read");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Вернуться в главное меню");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> System.out.println("Функция Create еще не реализована.");
                case "2" -> System.out.println("Функция Read еще не реализована.");
                case "3" -> System.out.println("Функция Update еще не реализована.");
                case "4" -> System.out.println("Функция Delete еще не реализована.");
                case "5" -> {
                    System.out.println("Возвращение в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private <T> void handleServiceOperations(T service) throws SQLException {
        while (true) {
            System.out.println("\nВыберите CRUD операцию:");
            System.out.println("1. Create");
            System.out.println("2. Read");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Вернуться в главное меню");
            String choice = scanner.nextLine();

            switch (choice) {
                //create
                case "1" -> {
                    //user
                    if (service instanceof UserService) {
                        User user = new User();
                        System.out.println("Введите id пользователя");
                        user.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите имя пользователя: ");
                        user.setName(scanner.nextLine());
                        System.out.println("Введите EMAIL пользователя: ");
                        user.setEmail(scanner.nextLine());
                        System.out.println("Введите путешествие пользователя: ");
                        user.setTrip(Long.parseLong(scanner.nextLine()));
                        ((UserService) service).createUser(user);
                    }
                    //trip
                    else if (service instanceof TripService) {
                        Trip trip = new Trip();
                        AttractionService attractionService = new AttractionService();
                        System.out.println("Введите id путешествия");
                        trip.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите стартовую локацию: ");
                        trip.setStartLocation(scanner.nextLine());
                        System.out.println("Введите конечную локацию: ");
                        trip.setEndLocation(scanner.nextLine());
                        System.out.println("Введите id достопримечательностей через , : ");
                        trip.setSelectedAttractions(Arrays.stream(scanner.nextLine().split(","))
                                .map(Long::parseLong)
                                .map(id -> {
                                    try {
                                        return attractionService.getAttractionById(id);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .filter(Objects::nonNull)
                                .toList());
                        ((TripService) service).addTrip(trip);
                    }
                    //attraction
                    else if (service instanceof AttractionService) {
                        Attraction attraction = new Attraction();
                        System.out.println("Введите id достопримечательности");
                        attraction.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите название достопримечательности: ");
                        attraction.setName(scanner.nextLine());
                        System.out.println("Введите локацию: ");
                        attraction.setLocation(scanner.nextLine());
                        System.out.println("Введите рейтинг: ");
                        attraction.setRating(Double.parseDouble(scanner.nextLine()));
                        ((AttractionService) service).addAttraction(attraction);
                    }
                    //transport
                    else if (service instanceof TransportService) {
                        Transport transport = new Transport();
                        System.out.println("Введите id транспорта");
                        transport.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите вид транспорта: ");
                        transport.setType(scanner.nextLine());
                        System.out.println("Введите расписание: ");
                        transport.setSchedule(scanner.nextLine());
                        System.out.println("Введите маршрут: ");
                        transport.setRoute(scanner.nextLine());
                        ((TransportService) service).addTransport(transport);
                    }

                }
                //read
                case "2" -> {
                    //user
                    if (service instanceof UserService) {
                        System.out.println("1. Получить пользователя по id");
                        System.out.println("2. Получить список всех пользователей");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id пользователя: ");
                                User user = ((UserService) service).getUser(Long.parseLong(scanner.nextLine()));
                                System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail());
                            }
                            case "2" -> {
                                List<User> userList = ((UserService) service).getAllUsers();
                                userList.forEach(user -> System.out.println(user.getId() + " " + user.getName() + " " + user.getEmail()));
                            }
                        }
                    }
                    //trip
                    if (service instanceof TripService) {
                        System.out.println("1. Получить путешествие по id");
                        System.out.println("2. Получить список всех путешествий");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id путешествия: ");
                                Trip trip = ((TripService) service).getTripById(Long.parseLong(scanner.nextLine()));
                                System.out.println(trip.getId() + " " + trip.getStartLocation() + " " + trip.getEndLocation() + " " + trip.getTripSchedule() + " " + trip.getSelectedAttractions());
                            }
                            case "2" -> {
                                List<Trip> tripList = ((TripService) service).getAllTrips();
                                tripList.forEach(trip -> System.out.println(trip.getId() + " " + trip.getStartLocation() + " " + trip.getEndLocation() + " " + trip.getTripSchedule()));
                            }
                        }
                    }
                    //attraction
                    if (service instanceof AttractionService) {
                        System.out.println("1. Получить достопримечательность по id");
                        System.out.println("2. Получить список всех достопримечательностей");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id достопримечательности: ");
                                Attraction attraction = ((AttractionService) service).getAttractionById(Long.parseLong(scanner.nextLine()));
                                System.out.println(attraction.getId() + " " + attraction.getName() + " " + attraction.getLocation() + " " + attraction.getRating());
                            }
                            case "2" -> {
                                List<Attraction> attractionList = ((AttractionService) service).getAllAttractions();
                                attractionList.forEach(attraction -> System.out.println(attraction.getId() + " " + attraction.getName() + " " + attraction.getLocation() + " " + attraction.getRating()));
                            }
                        }
                    }
                    //transport
                    if (service instanceof TransportService) {
                        System.out.println("1. Получить транспорт по id");
                        System.out.println("2. Получить список всего транспорта");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id транспорта: ");
                                Transport transport = ((TransportService) service).getTransportById(Long.parseLong(scanner.nextLine()));
                                System.out.println(transport.getId() + " " + transport.getType() + " " + transport.getSchedule() + " " + transport.getRoute());
                            }
                            case "2" -> {
                                List<Transport> transportList = ((TransportService) service).getAllTransports();
                                transportList.forEach(transport -> System.out.println(transport.getId() + " " + transport.getType() + " " + transport.getSchedule() + " " + transport.getRoute()));
                            }
                        }
                    }
                }
                //update
                case "3" -> {
                    if (service instanceof UserService) {
                        User user = new User();
                        System.out.println("Введите id пользователя");
                        user.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите имя пользователя: ");
                        user.setName(scanner.nextLine());
                        System.out.println("Введите EMAIL пользователя: ");
                        user.setEmail(scanner.nextLine());
                        System.out.println("Введите путешествие пользователя: ");
                        user.setTrip(Long.parseLong(scanner.nextLine()));
                        ((UserService) service).updateUser(user);
                    }
                    //trip
                    else if (service instanceof TripService) {
                        Trip trip = new Trip();
                        AttractionService attractionService = new AttractionService();
                        System.out.println("Введите id путешествия");
                        trip.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите стартовую локацию: ");
                        trip.setStartLocation(scanner.nextLine());
                        System.out.println("Введите конечную локацию: ");
                        trip.setEndLocation(scanner.nextLine());
                        System.out.println("Введите id достопримечательностей через , : ");
                        trip.setSelectedAttractions(Arrays.stream(scanner.nextLine().split(","))
                                .map(Long::parseLong)
                                .map(id -> {
                                    try {
                                        return attractionService.getAttractionById(id);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .filter(Objects::nonNull)
                                .toList());
                        ((TripService) service).updateTrip(trip);
                    }
                    //attraction
                    else if (service instanceof AttractionService) {
                        Attraction attraction = new Attraction();
                        System.out.println("Введите id достопримечательности");
                        attraction.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите название достопримечательности: ");
                        attraction.setName(scanner.nextLine());
                        System.out.println("Введите локацию: ");
                        attraction.setLocation(scanner.nextLine());
                        System.out.println("Введите рейтинг: ");
                        attraction.setRating(Double.parseDouble(scanner.nextLine()));
                        ((AttractionService) service).updateAttraction(attraction);
                    }
                    //transport
                    else if (service instanceof TransportService) {
                        Transport transport = new Transport();
                        System.out.println("Введите id транспорта");
                        transport.setId(Long.parseLong(scanner.nextLine()));
                        System.out.println("Введите вид транспорта: ");
                        transport.setType(scanner.nextLine());
                        System.out.println("Введите расписание: ");
                        transport.setSchedule(scanner.nextLine());
                        System.out.println("Введите маршрут: ");
                        transport.setRoute(scanner.nextLine());
                        ((TransportService) service).updateTransport(transport);
                    }

                }
                //delete
                case "4" -> {
                    //user
                    if (service instanceof UserService) {
                        System.out.println("1. Удалить пользователя по id");
                        System.out.println("2. Очистить таблицу");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id пользователя: ");
                                ((UserService) service).deleteUser(Long.parseLong(scanner.nextLine()));
                                System.out.println("Пользователь удалён");
                            }
                            case "2" -> {
                                ((UserService) service).clearTable();
                                System.out.println("Таблица очищена");
                            }
                        }
                    }
                    //trip
                    if (service instanceof TripService) {
                        System.out.println("1. Удалить путешествие по id");
                        System.out.println("2. Очистить таблицу");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id путешествия: ");
                                ((TripService) service).deleteTrip(Long.parseLong(scanner.nextLine()));
                                System.out.println("Путешествие удалено");
                            }
                            case "2" -> {
                                ((TripService) service).clearTable();
                                System.out.println("Таблица очищена");
                            }
                        }
                    }
                    //attraction
                    if (service instanceof AttractionService) {
                        System.out.println("1. Удалить достопримечательность по id");
                        System.out.println("2. Очистить таблицу");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id достопримечательности: ");
                                ((AttractionService) service).deleteAttraction(Long.parseLong(scanner.nextLine()));
                                System.out.println("Достопримечательность удалена");
                            }
                            case "2" -> {
                                ((AttractionService) service).clearTable();
                                System.out.println("Таблица очищена");
                            }
                        }
                    }
                    //transport
                    if (service instanceof TransportService) {
                        System.out.println("1. Удалить транспорт по id");
                        System.out.println("2. Очистить");
                        String readChoice = scanner.nextLine();
                        switch (readChoice) {
                            case "1" -> {
                                System.out.println("Введите id транспорта: ");
                                ((TransportService) service).deleteTransport(Long.parseLong(scanner.nextLine()));
                                System.out.println("Транспорт удалён");
                            }
                            case "2" -> {
                                ((TransportService) service).clearTable();
                                System.out.println("Таблица очищена");
                            }
                        }
                    }
                }
                case "5" -> {
                    System.out.println("Возвращение в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }
}
