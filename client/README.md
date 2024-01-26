# Client Structure
The client part of the project is all about what the user sees from the very beginning of launching the application, what features introduced in the app, and how these features are handled.

Following the Model-View-Controller (MVC) architecture, we have organized our project into three main components as follow:
### Model 
Represents the data and the business logic of the application. It consists of six files, each class represents information about a table in the database with the needed constructor/s, setters and getters.
### View 
Responsible for displaying the data to the user and presenting the user interface. It includes all screens the user interacts with throughout his experience with the app. The view screens were designed with `Scene Builder`
### Controller
Acts as an intermediary between the model and the view. It receives user input from the view, processes it, and updates the view accordingly. In our project, the controller handles user actions, initiate communication with the server (this connection will be explained below), and update the view based on the changes in the model. User actions are handled through methods assigned to each button or based on specific events (ex. user gets a notification when another user sends him a friend request).


Beside these components, others present to establish the connection with the server, provide needed resources:
### Connection
Initiates communication with the server in `MyConnection` file. In addition, it contains two other classes `MessageProtocol` and `RecieverHandler` which are part of organizing requests between the clinet and the server (explained below in the project standards).
### Resources
Contains all view-related files from photos to css files used in providing style to the user interface.
### Main
Responsible for launching the application.

# Client Standards


