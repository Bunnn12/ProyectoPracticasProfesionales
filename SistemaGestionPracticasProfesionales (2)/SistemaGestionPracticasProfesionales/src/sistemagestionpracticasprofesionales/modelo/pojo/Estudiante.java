package sistemagestionpracticasprofesionales.modelo.pojo;

/**
 *
 * @author rodri
 */
public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String genero;
    private String matricula;
    private String contrasenia;
    private String correo;
    private String telefono;
    private int idGrupo;
    private String grupo;
    private String nombreProyecto;
    private String nombreOV;
    public Estudiante() {}

    public Estudiante(int idEstudiante, String nombre, String apellidoPaterno, String apellidoMaterno, String genero, String matricula, String contrasenia, String correo, String telefono, int idGrupo, String grupo) {
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.genero = genero;
        this.matricula = matricula;
        this.contrasenia = contrasenia;
        this.correo = correo;
        this.telefono = telefono;
        this.idGrupo = idGrupo;
        this.grupo = grupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }
    
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getNombreOV() {
        return nombreOV;
    }
    
    public void setNombreOV(String nombreOV) {
        this.nombreOV = nombreOV;
    }
    
    public String getNombreCompleto() {
        String nombreCompuesto = nombre;
        if (apellidoPaterno != null) nombreCompuesto += " " + apellidoPaterno;
        if (apellidoMaterno != null) nombreCompuesto += " " + apellidoMaterno;
        return nombreCompuesto;
    }

}
