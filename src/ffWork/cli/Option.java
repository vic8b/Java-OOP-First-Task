package ffWork.cli;

public enum Option implements CliOption {
    ADD_USER(1, "Add user"),
    RESOURCES(2, "Resources menu"),
    BOOKINGS(3, "Bookings menu"),
    PRICING(4, "Pricing menu"),
    LIST_OF_USERS(5, "List of users"),
    LIST_OF_BOOKINGS(6, "List of bookings"),
    LIST_OF_RESOURCES(7, "List of resources"),
    HELP(8, "Help"),
    QUIT(0, "Quit");

    private final int optionNumber;
    private final String description;

    Option(int optionNumber, String description) {
        this.optionNumber = optionNumber;
        this.description = description;
    }

    public static Option fromNumber(int number) {
        Option option = null;

        for (Option optionValue : Option.values()) {
            if (optionValue.optionNumber == number) {
                option = optionValue;
            }
        }
        return option;
    }

    @Override
    public int getOptionNumber() {
        return optionNumber;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public enum User implements CliOption {
        ADD_USER_INDIVIDUAL(1, "Add individual user"),
        ADD_USER_COMPANY(2, "Add company user"),
        QUIT(0, "Quit");

        private final int optionNumber;
        private final String description;

        User(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        public static Option.User fromNumber(int number) {
            Option.User option = null;

            for (Option.User optionValue : Option.User.values()) {
                if (optionValue.optionNumber == number) {
                    option = optionValue;
                }
            }
            return option;
        }

        @Override
        public int getOptionNumber() {
            return optionNumber;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum Resource implements CliOption {
        ADD_ROOM(1, "Add room"),
        ADD_DESK(2, "Add desk"),
        ADD_DEVICE(3, "Add device"),
        QUIT(0, "Quit");

        private final int optionNumber;
        private final String description;

        Resource(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        public static Option.Resource fromNumber(int number) {
            Option.Resource option = null;

            for (Option.Resource optionValue : Option.Resource.values()) {
                if (optionValue.optionNumber == number) {
                    option = optionValue;
                }
            }
            return option;
        }

        @Override
        public int getOptionNumber() {
            return optionNumber;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum Booking implements CliOption {
        BOOK(1, "Book"),
        CONFIRM(2, "Confirm booking"),
        CANCEL(3, "Cancel booking"),
        QUIT(0, "Quit");

        private final int optionNumber;
        private final String description;

        Booking(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        public static Option.Booking fromNumber(int number) {
            Option.Booking option = null;

            for (Option.Booking optionValue : Option.Booking.values()) {
                if (optionValue.optionNumber == number) {
                    option = optionValue;
                }
            }
            return option;
        }

        @Override
        public int getOptionNumber() {
            return optionNumber;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public enum Pricing implements CliOption {
        SET_PRICING(1, "Set pricing"),
        PAY(2, "Pay"),
        INVOICE(3, "Invoice"),
        REFUND(4, "Refund"),
        QUIT(0, "Quit");

        private final int optionNumber;
        private final String description;

        Pricing(int optionNumber, String description) {
            this.optionNumber = optionNumber;
            this.description = description;
        }

        public static Option.Pricing fromNumber(int number) {
            Option.Pricing option = null;

            for (Option.Pricing optionValue : Option.Pricing.values()) {
                if (optionValue.optionNumber == number) {
                    option = optionValue;
                }
            }
            return option;
        }

        @Override
        public int getOptionNumber() {
            return optionNumber;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}

