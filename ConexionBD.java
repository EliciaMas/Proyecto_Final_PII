package SParcial;

import java.awt.Font;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javax.swing.JLabel;

public class ConexionBD extends Application {

    private static final String URL = "jdbc:mysql://localhost:3306/BDFloreriaLaElegancia";
    private static final String USER = "root";
    private static final String PASSWORD = "ma213027%ca";

    // Conexión a la base de datos
    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    // Insertar un producto en la base de datos
    public static void insertarProducto(int codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "INSERT INTO producto (codigoProducto, nombreProducto, precioUnitario, cantidadProducto, fechaVencimiento) VALUES (?,?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, codigo);
            pst.setString(2, nombre);
            pst.setDouble(3, precio);
            pst.setInt(4, cantidad);
            pst.setDate(5, java.sql.Date.valueOf(fecha));
            pst.executeUpdate();
            System.out.println("Producto insertado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar el producto: " + e.getMessage());
        }
    }

    // Listar los productos de la base de datos
    public static String[] listarProductos() {
        String query = "SELECT * FROM producto";
        ArrayList<String> productos = new ArrayList<>();
        try (Connection con = ConexionBD.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String producto = "Código: " + rs.getInt("codigoProducto") + ", Nombre: " + rs.getString("nombreProducto") + ", Precio: " + rs.getDouble("precioUnitario");
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los productos: " + e.getMessage());
        }
        return productos.toArray(new String[0]);
    }

    // Actualizar un producto
    public static void actualizarProducto(int codigo, String nombre, double precio, int cantidad, String fecha) {
        String query = "UPDATE producto SET nombreProducto = ?, precioUnitario = ?, cantidadProducto = ?, fechaVencimiento = ? WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nombre);
            pst.setDouble(2, precio);
            pst.setInt(3, cantidad);
            pst.setDate(4, java.sql.Date.valueOf(fecha));
            pst.setInt(5, codigo);
            pst.executeUpdate();
            System.out.println("Producto actualizado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
        }
    }

    // Buscar un producto
    public static String buscarProducto(int codigo) {
        String query = "SELECT * FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, codigo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return "Código: " + rs.getInt("codigoProducto") + ", Nombre: " + rs.getString("nombreProducto") + ", Precio: " + rs.getDouble("precioUnitario") + ", Cantidad: " + rs.getInt("cantidadProducto") + ", Fecha Vencimiento: " + rs.getDate("fechaVencimiento");
            } else {
                return "Producto no encontrado";
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el producto: " + e.getMessage());
            return "Error al buscar el producto";
        }
    }
    
    public static String buscarProductoPorNombre(String buscarNom) { 
    String query = "SELECT * FROM producto WHERE nombreProducto LIKE ?";
    try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
        // Usamos LIKE para permitir coincidencias parciales en el nombre
        pst.setString(1, "%" + buscarNom + "%");
        ResultSet rs = pst.executeQuery();
        
        // Si encontramos productos que coinciden
        if (rs.next()) {
            return "Código: " + rs.getInt("codigoProducto") + 
                   ", Nombre: " + rs.getString("nombreProducto") + 
                   ", Precio: " + rs.getDouble("precioUnitario") + 
                   ", Cantidad: " + rs.getInt("cantidadProducto") + 
                   ", Fecha Vencimiento: " + rs.getDate("fechaVencimiento");
        } else {
            return "Producto no encontrado";
        }
    } catch (SQLException e) {
        System.out.println("Error al buscar el producto: " + e.getMessage());
        return "Error al buscar el producto";
    }
}


    // Eliminar un producto
    public static void eliminarProducto(int codigo) {
        String query = "DELETE FROM producto WHERE codigoProducto = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, codigo);
            pst.executeUpdate();
            System.out.println("Producto eliminado correctamente");
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
        }
    }

    // Interfaz gráfica principal
    @Override
    public void start(Stage stage) {
        stage.setTitle("CRUD");
        
        // Establecer el favicon
    String faviconPath = "file:/C:/Users/DELL/Desktop/Progra%20II/Vacacion/Form%20Vacaciones/src/fr.jpg";  // Cambia esta ruta a la ubicación de tu imagen
    Image favicon = new Image(faviconPath);
    stage.getIcons().add(favicon);

        // Ruta de la imagen de fondo
        String imagePath = "file:/C:/Users/DELL/Desktop/Progra%20II/Vacacion/Form%20Vacaciones/src/fr.jpg";  // Asegúrate de que esta ruta sea correcta
        
        // Cargar la imagen
        Image image = new Image(imagePath);
        BackgroundImage backgroundImage = new BackgroundImage(image, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, 
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, false));

        // Layout principal
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        // Asignar el fondo a la VBox
        root.setBackground(new Background(backgroundImage));
        
         // Crear un título personalizado con fuente y color
      Button btnC = new Button("CRUD");
    btnC.setStyle(
        "-fx-font-size: 75px; " +
        "-fx-padding: 3 23; " +              // Ajuste de padding (márgenes internos)
        "-fx-text-fill: #bdff4a; " +         // Color personalizado para el texto (en este caso)
        "-fx-font-weight: bold; " +          // Texto en negrita
        "-fx-font-family: 'Georgia'; " +     // Fuente personalizada
        "-fx-border-width: 0; " +            // Eliminar borde alrededor del botón
        "-fx-focus-color: transparent; " +   // Eliminar color de borde cuando esté en foco
        "-fx-background-color: transparent; " + // Fondo transparente
        "-fx-background-insets: 0; "         // Eliminar cualquier margen extra alrededor del fondo
    );


        // Botones para cada acción
        Button btnIngresar = new Button("Ingresar Producto");
        btnIngresar.setStyle("-fx-font-size: 16px; -fx-padding: 3 23; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");  
        btnIngresar.setOnAction(e -> showIngresarProducto(stage));

        Button btnMostrar = new Button("Mostrar Productos");
        btnMostrar.setStyle("-fx-font-size: 16px; -fx-padding: 3 21; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnMostrar.setOnAction(e -> showMostrarProductos(stage));

        Button btnActualizar = new Button("Actualizar Producto");
        btnActualizar.setStyle("-fx-font-size: 16px; -fx-padding: 3 18; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnActualizar.setOnAction(e -> showActualizarProducto(stage));

        Button btnBuscar = new Button("Buscar por Código ");
        btnBuscar.setStyle("-fx-font-size: 16px; -fx-padding: 3 23; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnBuscar.setOnAction(e -> showBuscarProducto(stage));
        
        Button btnBuscarNom = new Button("Buscar por Nombre ");
        btnBuscarNom.setStyle("-fx-font-size: 16px; -fx-padding: 3 18; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnBuscarNom.setOnAction(e -> showBuscarNom(stage));

        Button btnEliminar = new Button("Eliminar Producto");
        btnEliminar.setStyle("-fx-font-size: 16px; -fx-padding: 3 23; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnEliminar.setOnAction(e -> showEliminarProducto(stage));

        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-font-size: 16px; -fx-padding: 3 23; -fx-background-color: #DAF7A6; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-family: 'Georgia';");
        btnSalir.setOnAction(e -> System.exit(0));

        root.getChildren().addAll(btnC,btnIngresar, btnMostrar, btnActualizar, btnBuscar,btnBuscarNom, btnEliminar, btnSalir);

        // Crear la escena con la VBox que tiene la imagen de fondo
        Scene scene = new Scene(root, 320, 350);
        stage.setScene(scene);
        stage.show();
    }

    // Mostrar la interfaz para ingresar un producto
   private void showIngresarProducto(Stage stage) {
    VBox ingresarLayout = new VBox(15);  // 15px de espacio entre los elementos verticales
    ingresarLayout.setAlignment(Pos.CENTER);

    // Fondo de la ventana
    String imagenRuta = "file:/C:/Users/DELL/Desktop/Progra II/Vacacion/Form Vacaciones/src/fo.jpg"; // Ruta de tu imagen
    Image imagenFondo = new Image(imagenRuta);
    BackgroundImage backgroundImage = new BackgroundImage(
        imagenFondo,
        BackgroundRepeat.NO_REPEAT,  // No repetir la imagen
        BackgroundRepeat.NO_REPEAT,  // No repetir la imagen
        BackgroundPosition.CENTER,   // Centrar la imagen
        BackgroundSize.DEFAULT       // Usar tamaño por defecto
    );
    Background fondo = new Background(backgroundImage);
    ingresarLayout.setBackground(fondo);  // Establecer el fondo de la VBox

    // Título
    Label labelIngresar = new Label("Ingresar Producto");
    labelIngresar.setStyle(
        "-fx-font-size: 30px; " +                  // Tamaño de la fuente
        "-fx-padding: 3 18; " +                     // Espaciado alrededor del texto (top, right, bottom, left)
        "-fx-text-fill: #0d486a; " +                // Color del texto
        "-fx-font-weight: bold; " +                 // Peso de la fuente (negrita)
        "-fx-font-family: 'Georgia';"               // Familia de la fuente
    );

    labelIngresar.setPadding(new javafx.geometry.Insets(0, 0, 10 , 0));  // Espacio debajo del título

    // Crear un HBox para cada campo con su respectivo label
    HBox hboxCodigo = new HBox(10);
    Label labelCodigo = new Label("Código:");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código del Producto");
    codigoField.setStyle("-fx-pref-width: 175px; -fx-border-radius: 5px; -fx-padding: 5px; -fx-border-color: #ccc;");
    hboxCodigo.getChildren().addAll(labelCodigo, codigoField);
    HBox.setMargin(labelCodigo, new Insets(0, 0, 0, 10));  // Margen a la izquierda

    HBox hboxNombre = new HBox(10);
    Label labelNombre = new Label("Nombre:");
    TextField nombreField = new TextField();
    nombreField.setPromptText("Nombre del Producto");
    nombreField.setStyle("-fx-pref-width: 175px; -fx-border-radius: 5px; -fx-padding: 5px; -fx-border-color: #ccc;");
    hboxNombre.getChildren().addAll(labelNombre, nombreField);
    HBox.setMargin(labelNombre, new Insets(0, 0, 0, 10));  // Margen a la izquierda

    HBox hboxPrecio = new HBox(10);
    Label labelPrecio = new Label("Precio:");
    TextField precioField = new TextField();
    precioField.setPromptText("Precio");
    precioField.setStyle("-fx-pref-width: 175px; -fx-border-radius: 5px; -fx-padding: 5px; -fx-border-color: #ccc;");
    hboxPrecio.getChildren().addAll(labelPrecio, precioField);
    HBox.setMargin(labelPrecio, new Insets(0, 0, 0, 10));  // Margen a la izquierda

    HBox hboxCantidad = new HBox(10);
    Label labelCantidad = new Label("Cantidad:");
    TextField cantidadField = new TextField();
    cantidadField.setPromptText("Cantidad");
    cantidadField.setStyle("-fx-pref-width: 175px; -fx-border-radius: 5px; -fx-padding: 5px; -fx-border-color: #ccc;");
    hboxCantidad.getChildren().addAll(labelCantidad, cantidadField);
    HBox.setMargin(labelCantidad, new Insets(0, 0, 0, 10));  // Margen a la izquierda

    HBox hboxFecha = new HBox(10);
    Label labelFecha = new Label("Fecha de Vencimiento:");
    TextField fechaField = new TextField();
    fechaField.setPromptText("Vec. (yyyy-MM-dd)");
    fechaField.setStyle("-fx-pref-width: 175px; -fx-border-radius: 5px; -fx-padding: 5px; -fx-border-color: #ccc;");
    hboxFecha.getChildren().addAll(labelFecha, fechaField);
    HBox.setMargin(labelFecha, new Insets(0, 0, 0, 10));  // Margen a la izquierda

    // Botón para ingresar el producto
    Button btnIngresar = new Button("Ingresar");
    btnIngresar.setStyle("-fx-background-color: #0d486a; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnIngresar.setOnAction(e -> {
        try {
            int codigo = Integer.parseInt(codigoField.getText());
            String nombre = nombreField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int cantidad = Integer.parseInt(cantidadField.getText());
            String fecha = fechaField.getText();
            insertarProducto(codigo, nombre, precio, cantidad, fecha);
            showAlert("Producto ingresado correctamente");
            showMostrarProductos(stage);
        } catch (NumberFormatException ex) {
            showAlert("Por favor ingrese valores válidos");
        }
    });

    // Botón para volver al menú principal
    Button btnVolver = new Button("Volver al Menú");
    btnVolver.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnVolver.setOnAction(e -> start(stage));

    // Agregar todos los elementos al layout
    ingresarLayout.getChildren().addAll(labelIngresar, hboxCodigo, hboxNombre, hboxPrecio, hboxCantidad, hboxFecha, btnIngresar, btnVolver);

    // Establecer un margen en el VBox para asegurarse de que haya espacio entre los elementos y los bordes de la ventana
    ingresarLayout.setPadding(new Insets(20, 20, 20, 20));  // 20px de margen alrededor del VBox

    // Crear la escena con el layout
    Scene ingresarScene = new Scene(ingresarLayout, 370, 390);
    stage.setScene(ingresarScene);
}
    // Mostrar los productos en la interfaz gráfica
    private void showMostrarProductos(Stage stage) {
        VBox mostrarLayout = new VBox(8);
        mostrarLayout.setAlignment(Pos.CENTER);

        ListView<String> productList = new ListView<>();
        ObservableList<String> products = FXCollections.observableArrayList(listarProductos());
        productList.setItems(products);

        // Botón para volver al menú
        Button btnVolver = new Button("Volver al Menú");
        btnVolver.setOnAction(e -> start(stage));

        Label labelListado = new Label("Listado de Productos");

// Aplicar el estilo (color de texto, fuente, tamaño, etc.)
labelListado.setStyle(
    "-fx-font-size: 24px; " +  // Tamaño de fuente
    "-fx-text-fill: #2C3E50; " +  // Color del texto (puedes cambiar el color por el que desees)
    "-fx-font-weight: bold; " +  // Peso de la fuente
    "-fx-font-family: 'Georgia';"  // Tipo de fuente (puedes cambiar 'Arial' por cualquier otra fuente disponible)
);
mostrarLayout.getChildren().addAll(labelListado, productList, btnVolver);
        Scene mostrarScene = new Scene(mostrarLayout, 350, 300);
        stage.setScene(mostrarScene);
    }

    // Mostrar la interfaz para actualizar un producto
private void showActualizarProducto(Stage stage) {
    VBox actualizarLayout = new VBox(15);  // Espaciado de 15px entre los elementos
    actualizarLayout.setAlignment(Pos.CENTER);

    // Establecer el fondo de la ventana
    String imagenRuta = "file:/C:/Users/DELL/Desktop/Progra II/Vacacion/Form Vacaciones/src/fo.jpg";
    Image imagenFondo = new Image(imagenRuta);
    BackgroundImage backgroundImage = new BackgroundImage(
        imagenFondo,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT
    );
    Background fondo = new Background(backgroundImage);
    actualizarLayout.setBackground(fondo);

    // Título
    Label labelActualizar = new Label("Actualizar Producto");
    labelActualizar.setStyle(
        "-fx-font-size: 30px; " + 
        "-fx-padding: 3 18; " +
        "-fx-text-fill: #0d486a; " +
        "-fx-font-weight: bold; " +
        "-fx-font-family: 'Georgia';"
    );

    // Campos de entrada (con márgenes y estilo)
    HBox hboxCodigo = new HBox(10);
    Label labelCodigo = new Label("Código:");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código del Producto");
    codigoField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxCodigo.getChildren().addAll(labelCodigo, codigoField);

    HBox hboxNombre = new HBox(10);
    Label labelNombre = new Label("Nombre:");
    TextField nombreField = new TextField();
    nombreField.setPromptText("Nombre del Producto");
    nombreField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxNombre.getChildren().addAll(labelNombre, nombreField);

    HBox hboxPrecio = new HBox(10);
    Label labelPrecio = new Label("Precio:");
    TextField precioField = new TextField();
    precioField.setPromptText("Precio");
    precioField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxPrecio.getChildren().addAll(labelPrecio, precioField);

    HBox hboxCantidad = new HBox(10);
    Label labelCantidad = new Label("Cantidad:");
    TextField cantidadField = new TextField();
    cantidadField.setPromptText("Cantidad");
    cantidadField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxCantidad.getChildren().addAll(labelCantidad, cantidadField);

    HBox hboxFecha = new HBox(10);
    Label labelFecha = new Label("Fecha de Vencimiento:");
    TextField fechaField = new TextField();
    fechaField.setPromptText("Vec. (yyyy-MM-dd)");
    fechaField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxFecha.getChildren().addAll(labelFecha, fechaField);

    // Botón de actualización
    Button btnActualizar = new Button("Actualizar");
    btnActualizar.setStyle("-fx-background-color: #0d486a; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnActualizar.setOnAction(e -> {
        try {
            int codigo = Integer.parseInt(codigoField.getText());
            String nombre = nombreField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int cantidad = Integer.parseInt(cantidadField.getText());
            String fecha = fechaField.getText();
            actualizarProducto(codigo, nombre, precio, cantidad, fecha);
            showAlert("Producto actualizado correctamente");
            showMostrarProductos(stage);
        } catch (NumberFormatException ex) {
            showAlert("Por favor ingrese valores válidos");
        }
    });

    // Botón para volver al menú
    Button btnVolver = new Button("Volver al Menú");
    btnVolver.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnVolver.setOnAction(e -> start(stage));

    // Agregar todos los elementos al VBox
    actualizarLayout.getChildren().addAll(
        labelActualizar, hboxCodigo, hboxNombre, hboxPrecio, hboxCantidad, hboxFecha, btnActualizar, btnVolver
    );

    // Establecer el padding del VBox (alrededor de todos los elementos)
    actualizarLayout.setPadding(new Insets(20));  // Un padding de 20px para asegurar el margen adecuado desde el borde

    // Crear la escena y establecerla en el escenario
    Scene actualizarScene = new Scene(actualizarLayout, 390, 370);
    stage.setScene(actualizarScene);
}

private void showBuscarProducto(Stage stage) {
    VBox buscarLayout = new VBox(15);
    buscarLayout.setAlignment(Pos.CENTER);

    // Fondo de la ventana
    String imagenRuta = "file:/C:/Users/DELL/Desktop/Progra II/Vacacion/Form Vacaciones/src/fo.jpg";
    Image imagenFondo = new Image(imagenRuta);
    BackgroundImage backgroundImage = new BackgroundImage(
        imagenFondo,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT
    );
    Background fondo = new Background(backgroundImage);
    buscarLayout.setBackground(fondo);

    // Título
    Label labelBuscar = new Label("Buscar Producto");
    labelBuscar.setStyle(
        "-fx-font-size: 30px; " +
        "-fx-padding: 3 18; " +
        "-fx-text-fill: #0d486a; " +
        "-fx-font-weight: bold; " +
        "-fx-font-family: 'Georgia';"
    );

    // Campo para el código
    HBox hboxCodigo = new HBox(10);
    Label labelCodigo = new Label("Código a buscar:");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código del Producto");
    codigoField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxCodigo.getChildren().addAll(labelCodigo, codigoField);

    // Botón de búsqueda
    Button btnBuscar = new Button("Buscar");
    btnBuscar.setStyle("-fx-background-color: #0d486a; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnBuscar.setOnAction(e -> {
        try {
            int codigo = Integer.parseInt(codigoField.getText());
            String result = buscarProducto(codigo);
            showAlert(result);
        } catch (NumberFormatException ex) {
            showAlert("Por favor ingrese un código válido");
        }
    });

    // Botón para volver
    Button btnVolver = new Button("Volver al Menú");
    btnVolver.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnVolver.setOnAction(e -> start(stage));

    // Agregar todo al layout
    buscarLayout.getChildren().addAll(labelBuscar, hboxCodigo, btnBuscar, btnVolver);
    buscarLayout.setPadding(new Insets(20)); // Añadir margen a la ventana

    // Crear la escena
    Scene buscarScene = new Scene(buscarLayout, 380, 250);
    stage.setScene(buscarScene);
}


private void showBuscarNom(Stage stage) {
    VBox buscarLayout = new VBox(15);  // 15px de espacio entre los elementos
    buscarLayout.setAlignment(Pos.CENTER);

    // Fondo de la ventana
    String imagenRuta = "file:/C:/Users/DELL/Desktop/Progra II/Vacacion/Form Vacaciones/src/fo.jpg";
    Image imagenFondo = new Image(imagenRuta);
    BackgroundImage backgroundImage = new BackgroundImage(
        imagenFondo,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT
    );
    Background fondo = new Background(backgroundImage);
    buscarLayout.setBackground(fondo);

    // Título
    Label labelBuscar = new Label("Buscar por Nombre");
    labelBuscar.setStyle(
        "-fx-font-size: 30px; " + 
        "-fx-padding: 3 18; " +
        "-fx-text-fill: #0d486a; " +
        "-fx-font-weight: bold; " +
        "-fx-font-family: 'Georgia';"
    );

    // Campo para el nombre
    HBox hboxNombre = new HBox(10);
    Label labelNombre = new Label("Nombre a buscar:");
    TextField nombreField = new TextField();
    nombreField.setPromptText("Nombre del Producto");
    nombreField.setStyle("-fx-pref-width: 150px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxNombre.getChildren().addAll(labelNombre, nombreField);

    // Botón de búsqueda
    Button btnBuscar = new Button("Buscar");
    btnBuscar.setStyle("-fx-background-color: #0d486a; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnBuscar.setOnAction(e -> {
        String buscarNom = nombreField.getText().trim();
        if (!buscarNom.isEmpty()) {
            String result = buscarProductoPorNombre(buscarNom);
            showAlert(result);
        } else {
            showAlert("Por favor ingrese un nombre válido");
        }
    });

    // Botón para volver al menú
    Button btnVolver = new Button("Volver al Menú");
    btnVolver.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnVolver.setOnAction(e -> start(stage));

    // Agregar todos los elementos al VBox
    buscarLayout.getChildren().addAll(
        labelBuscar, hboxNombre, btnBuscar, btnVolver
    );

    // Establecer un margen alrededor del VBox
    buscarLayout.setPadding(new Insets(20));  // 20px de margen

    // Crear la escena y establecerla en el escenario
    Scene buscarScene = new Scene(buscarLayout, 390, 250);
    stage.setScene(buscarScene);
}

private void showEliminarProducto(Stage stage) {
    VBox eliminarLayout = new VBox(15);  // Espaciado de 15px entre los elementos
    eliminarLayout.setAlignment(Pos.CENTER);

    // Fondo de la ventana
    String imagenRuta = "file:/C:/Users/DELL/Desktop/Progra II/Vacacion/Form Vacaciones/src/fo.jpg";
    Image imagenFondo = new Image(imagenRuta);
    BackgroundImage backgroundImage = new BackgroundImage(
        imagenFondo,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        BackgroundSize.DEFAULT
    );
    Background fondo = new Background(backgroundImage);
    eliminarLayout.setBackground(fondo);

    // Título
    Label labelEliminar = new Label("Eliminar Producto");
    labelEliminar.setStyle(
        "-fx-font-size: 30px; " + 
        "-fx-padding: 3 18; " +
        "-fx-text-fill: #0d486a; " +
        "-fx-font-weight: bold; " +
        "-fx-font-family: 'Georgia';"
    );

    // Campo para el código del producto
    HBox hboxCodigo = new HBox(10);
    Label labelCodigo = new Label("Código:");
    TextField codigoField = new TextField();
    codigoField.setPromptText("Código del Producto");
    codigoField.setStyle("-fx-pref-width: 250px; -fx-border-radius: 5px; -fx-padding: 5px;");
    hboxCodigo.getChildren().addAll(labelCodigo, codigoField);

    // Botón para eliminar el producto
    Button btnEliminar = new Button("Eliminar");
    btnEliminar.setStyle("-fx-background-color: #0d486a; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnEliminar.setOnAction(e -> {
        try {
            int codigo = Integer.parseInt(codigoField.getText());
            eliminarProducto(codigo);
            showAlert("Producto eliminado correctamente");
            showMostrarProductos(stage);
        } catch (NumberFormatException ex) {
            showAlert("Por favor ingrese un código válido");
        }
    });

    // Botón para volver al menú
    Button btnVolver = new Button("Volver al Menú");
    btnVolver.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px;");
    btnVolver.setOnAction(e -> start(stage));

    // Agregar todos los elementos al VBox
    eliminarLayout.getChildren().addAll(
        labelEliminar, hboxCodigo, btnEliminar, btnVolver
    );

    // Establecer un margen alrededor del VBox
    eliminarLayout.setPadding(new Insets(20));  // 20px de margen

    // Crear la escena y establecerla en el escenario
    Scene eliminarScene = new Scene(eliminarLayout, 370, 250);
    stage.setScene(eliminarScene);
}


    // Método para mostrar alertas
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Ejecutar la aplicación
    public static void main(String[] args) {
        launch(args);
    }
}
