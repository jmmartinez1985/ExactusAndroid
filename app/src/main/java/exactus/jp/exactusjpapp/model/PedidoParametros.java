package exactus.jp.exactusjpapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JM on 10/07/2015.
 */
public class PedidoParametros {

    public PedidoParametros(){
        PEDIDODETALLE = new ArrayList<PedidoLineaParametros>();
    }

    public String CLIENTE;
    public String BODEGA;
    public String ORDEN_COMPRA;
    public String OBSERVACIONES;
    public String USUARIO_LOGIN;
    public String TARJETA_CREDITO;
    public String NOMBRE_CUENTA; 
    public String PEDIDO;
    public int CONDICION_PAGO;
    public String CODIGO_CONSECUTIVO;
    public List<PedidoLineaParametros> PEDIDODETALLE;


}
