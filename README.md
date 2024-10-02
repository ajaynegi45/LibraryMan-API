# LibraryMan: Library Management Simplified 📚

LibraryMan is a user-friendly software solution for schools, companies, and libraries to efficiently manage book collections, track borrowing, and monitor due dates. It streamlines operations, reduces errors, and enhances the lending experience, making it easy to maintain a well-organized library.

## Demo

Explore the frontend of the LibraryMan project [here](https://github.com/ajaynegi45/LibraryMan).

## Project Structure 📂
```
LibraryMan/
│
├── api-docs/
├── notification-docs/
├── project-structure/
├── src/
│   ├── main/
│   └── test/
│
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
│
├── pom.xml
└── README.md
```
Checkout related [Diagrams](https://github.com/ajaynegi45/LibraryMan-API/tree/main/project-structure)

### Key Modules
- **src/main**: Contains the main application code.
- **src/test**: Contains unit tests for the application.
- **pom.xml**: Maven project file for dependency management.

## API Endpoints 🔗

Want to know more about our API endpoints? Check out our [API Documentation](https://github.com/ajaynegi45/LibraryMan-API/tree/main/api-docs) for detailed information.

#### Test Endpoints

Ready to try out our API endpoints? Use [Postman Documentation](https://documenter.getpostman.com/view/28691426/2sAXjJ6D7L) to test and explore our APIs.

## How to Run the Project 💨

1. Ensure you have Java and MySQL installed on your system.
2. Clone or download the project from the repository.
   
    ```bash
   git clone https://github.com/ajaynegi45/LibraryMan-API.git
    
3. Import the project into your preferred IDE (e.g., Eclipse, IntelliJ).
4. Set up the MySQL database:
    ```bash
   Create a new database named libraryman
    ```
    Update the database configurations in the `application-development.properties` file with your database configurations:
    ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/libraryman
    spring.datasource.username=your-username
    spring.datasource.password=your-password
5. Build and run the project using the IDE or run the following command from the project root directory:
    ```bash
    mvn spring-boot:run

## ‼️ Important Note ‼️

- You need to set up the database and make sure the application properties are correctly configured to run the project successfully.

## Upcoming Update
Adding more features, error handling, authentication, and security measures.

## Contributing 🤗
We welcome contributions to the LibraryMan project! To contribute:

- Fork the repository.
- Create a new branch for your feature/bug fix.
- Ensure your code follows our coding standards.
- Submit a pull request with a description of your changes.

Please see [CONTRIBUTING.md](https://github.com/ajaynegi45/Library-API/blob/main/contributing.md) for more ways to get started, and adhere to our [CODE_OF_CONDUCT.md](https://github.com/ajaynegi45/Library-API/blob/main/code_of_conduct.md).

Contributions are always welcome! ✨

## Contact Information 📧

If you have any questions or would like to connect, please don't hesitate to reach out. I'd be more than happy to chat and learn from your experiences too.
<br><br>
**LinkedIn:** [Connect with me](https://www.linkedin.com/in/ajaynegi45/)

## Contributors

<a href = "https://github.com/ajaynegi45/LibraryMan-API/graphs/contributors">
  <img src = "https://contrib.rocks/image?repo=ajaynegi45/LibraryMan-API"/>
</a>

### Stargazers

<p align="center">
  <i>If you like LibraryMan Project, please <a href="https://github.com/ajaynegi45/LibraryMan-API/stargazers">★</a> star this repository to show your support! 🤩</i>
 <br/>
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=ajaynegi45/LibraryMan-API&type=Date&theme=dark" />
      <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=ajaynegi45/LibraryMan-API&type=Date" />
      <img align="center" alt="Star History Chart" src="https://api.star-history.com/svg?repos=ajaynegi45/LibraryMan-API&type=Date" />
    </picture>
</p>

## Acknowledgments
Thank you for taking the time to explore my project. I hope you find them informative and useful in your journey to learn Java and enhance your programming skills. Your support and contributions are highly appreciated.
<br>
Happy coding! ✨

## License
This project is licensed under the MIT License.
