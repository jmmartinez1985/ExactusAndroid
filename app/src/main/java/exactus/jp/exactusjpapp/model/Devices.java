package exactus.jp.exactusjpapp.model;

import java.util.ArrayList;

/**
 * Created by JM on 10/03/2015.
 */
public class Devices {

    public Devices(){

        opcionesList = new ArrayList<Opciones>();
    }


    public int IDDispositivo;
    public int IDEmpresa;
    public String NombreDispositivo;
    public String MACDispositivo;
    public String FechaExpira;
    public Boolean Activo;
    public int UsuarioModifica;
    public String FechaModifica;

    public Empresa empresaObject;
    public ArrayList<Opciones> opcionesList;

}
