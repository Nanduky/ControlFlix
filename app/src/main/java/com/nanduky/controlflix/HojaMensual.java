package com.nanduky.controlflix;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nanduky.controlflix.utilidades.Utilidades;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;
// ...


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HojaMensual.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HojaMensual#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HojaMensual extends Fragment implements ScaleGestureDetector.OnScaleGestureListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HojaMensual() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HojaMensual.
     */
    // TODO: Rename and change types and number of parameters
    public static HojaMensual newInstance(String param1, String param2) {
        HojaMensual fragment = new HojaMensual();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /* ****************************   NUEVOS ACCEOS   ******************************************** */
    View vista;
    //fragmentlayout
    RelativeLayout relativeConHojaMensual;
    //scroll
    ScrollView scrollConHojaMensual ;
    HorizontalScrollView scrollHorizontalHojaMensual;
    //el tableLayaut para la hoja mensual
    TableLayout tbly_hoja_mensual;
    //Para las filas de la tabla
    TableRow trCabecera;
    //Los textView que hacen de columnas
    TextView tvTablaId,
            tvTablaFecha, tvTablaDia, tvTablaHoraIni, tvTablaHoraFin ,tvTablaTotalHoras,
            tvTablaADR, tvTablaDesayuno, tvTablaComida, tvTablaCena, tvTablaDormir,
            tvTablaTotalDieta, tvTablaExtra, tvTablaSeparador1, tvTablaSeparador2;

    TextView tvTablaTotalHorasMes, tvTablaTotalDiasADR, tvTablaTotalDesayuno, tvTablaTotalComida,
                tvTablaTotalCena, tvTablaTotalDormir, tvTablaExtraTotal, tvTablaSeparadorTotales;


    //Vadiable para ir acumulando el total de horas para el "cursor"
    private String stAcumuladorDeHoras = "00:00";
    //Variable que contendrá el numero de días trabajados con adr
    private String stNumeroDeDiasADR = "0" ;
    //Variables para los acumuladores de las dietas.
    private String stPrivateDesayuno = "0";
    private String stPrivateComida = "0";
    private String stPrivateCena = "0";
    private String stPrivateDormir = "0";
    private String stPrivateTotalDietas = "0";
    //Variable para los extras
    private String stPrivateExtras = "0";
    //variables para la disponibilidad y el total de extras
    private String stPrivateDisponivilidad = "0";
    private String stPrivateTotalExtras = "0";
    //crear un formato
    DecimalFormat df = new DecimalFormat("#0.00");
    //varirable boleana para comprovar si hay que mostra la disponibilidad o mo.
    private boolean booDisponivilidadVisible = false;

    /** Crear conexión con la base de datos **/

    //el nombre de la base de datos será db_fichaMensual
    ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);

    //CONSULTAS DB
    //Todos los registros del ultimo mes del ultimo año
    String queryTodosRegMayorYearMes = "SELECT * FROM " + Utilidades.TABLA_FICHAMENSUAL
            + " WHERE "
            + Utilidades.CAMPO_NUM_YEAR + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR
            + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
            + ") AND "
            +  Utilidades.CAMPO_NUM_MES + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_MES
            + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + " WHERE " + Utilidades.CAMPO_NUM_YEAR +
            " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + "))";
    //Cuenta los registros del el ultimo mes del año mas grande
    String queryCuentaRegistrosMayorYearMes = "SELECT COUNT(*) FROM " + Utilidades.TABLA_FICHAMENSUAL
            + " WHERE "
            + Utilidades.CAMPO_NUM_YEAR + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR
            + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
            + ") AND "
            +  Utilidades.CAMPO_NUM_MES + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_MES
            + ") FROM " + Utilidades.TABLA_FICHAMENSUAL +  " WHERE " + Utilidades.CAMPO_NUM_YEAR +
            " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + "))";
    //variables para guardar el numero de index de la columna
    private int intID = 0, intFecha = 0, intOtrasActi = 0, intOtrasActiRetri = 0,intDia = 0,intHoraIni = 0,intHoraFin = 0,intTotalHoras = 0,intADR = 0,
            intDesayuno = 0,intComida = 0,intCena = 0,intDormir = 0,intTotalDiets = 0,intExtras = 0;

    //boleano para borrar o no el dia seleccionado
    Boolean booBorrarDia = null;
    //variable para recoger la id del dia seleccionado para el borrado
    private String stIDdiaSelect = "";


    /* ************* zona multi selección ************* */
//los View de la multiselección para el borrado de registros
    LinearLayout lyMenuBorrarMultiple;
    TextView tvCancelarSelect,tvContadorSelect;
    ImageView ivCheckSelect, ivBorrarSelect;

    //boleano para comprobar si se esta haciendo multiseleccion de registros para borra
    private Boolean booMultiselec = false;
    //booleano para saber si se seleccionó el registro para marcar/desmarcar
    private Boolean booCheckSelect = false;
    //variable para recoger el numero de registros del mes para mostrarlo en el textView de la multiselección
    private String stNumeroRegMultiSelect = "0";
    //variable contador de registros seleccionados
    private int intContadorRegMultiSelect = 0;
    //ArrayList para almacenar los indices de los registros seleccionados
    ArrayList<String> alStRegSelect = new ArrayList<>();

    //se crea una array con los indices de la tabla
    private int[] arrColumnas = {1,2,3,4,5,7,8,9,10,11,12,14};

    View vCapturaDatosTabla;


    /* ************* zona multi seleccion ************* */


    /* ****************************   NUEVOS ACCEOS  FIN ***************************************** */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        vista  = inflater.inflate(R.layout.fragment_hoja_mensual, container, false);
        /* ***************************  ON CREATE  ********************************************* */

        //cuando se gira la pantalla se le dice que vuelva a cargar la hoja mensual
        if(savedInstanceState != null){
            //volver a inicio
            Fragment fragmentHojaMensual =new HojaMensual();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragmentHojaMensual);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        //el framentLayaut
        relativeConHojaMensual = vista.findViewById(R.id.relativeConHojaMensual);
        //el scroll
        scrollConHojaMensual = vista.findViewById(R.id.scrollConHojaMensual);
        scrollHorizontalHojaMensual = vista.findViewById(R.id.scrollHorizontalHojaMensual);
        //el tableLayaut de la hoja mensual
        tbly_hoja_mensual = vista.findViewById(R.id.tbly_hoja_mensual);
        //la fila cabecera
        trCabecera = vista.findViewById(R.id.trCabecera);
        //las columnas
        tvTablaId         = vista.findViewById(R.id.tvTablaId);
        tvTablaFecha      = vista.findViewById(R.id.tvTablaFecha);
        tvTablaDia        = vista.findViewById(R.id.tvTablaDia);
        tvTablaHoraIni    = vista.findViewById(R.id.tvTablaHoraIni );
        tvTablaHoraFin    = vista.findViewById(R.id.tvTablaHoraFin);
        tvTablaTotalHoras = vista.findViewById(R.id.tvTablaTotalHoras);
        tvTablaADR        = vista.findViewById(R.id.tvTablaADR);
        tvTablaDesayuno   = vista.findViewById(R.id.tvTablaDesayuno);
        tvTablaComida     = vista.findViewById(R.id.tvTablaComida);
        tvTablaCena       = vista.findViewById(R.id.tvTablaCena);
        tvTablaDormir     = vista.findViewById(R.id.tvTablaDormir);
        tvTablaTotalDieta = vista.findViewById(R.id.tvTablaTotalDieta);
        tvTablaExtra      = vista.findViewById(R.id.tvTablaExtra);
        tvTablaSeparador1 = vista.findViewById(R.id.tvTablaSeparador1);
        tvTablaSeparador2 = vista.findViewById(R.id.tvTablaSeparador2);

        //LinealLayout para el menú de borrar
        lyMenuBorrarMultiple = vista.findViewById(R.id.lyMenuBorrarMultiple);
        tvCancelarSelect = vista.findViewById(R.id.tvCancelarSelect);
        tvContadorSelect = vista.findViewById(R.id.tvContadorSelect);
        ivCheckSelect = vista.findViewById(R.id.ivCheckSelect);
        ivBorrarSelect = vista.findViewById(R.id.ivBorrarSelect);


        //Llama al metodo que cuenta/consulta que cuenta los dias de adr
        consultasDB();
        //para llamar al metodo de construir el tableLayaut dinamico
        TableLayoutDinamicoHojaMensual();

        vista.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MASK) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        //do something
                        Toast.makeText(getActivity(),"ZOOM up",Toast.LENGTH_SHORT).show();
                        Log.i("A", "001");
                    }
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        //do something
                        Toast.makeText(getActivity(),"ZOOM dow",Toast.LENGTH_SHORT).show();
                        Log.i("A", "001");
                    }
                }
                Log.i("A", "001");
                return true;
            }
        });



        /* ***************************  ON CREATE FIN ****************************************** */
        return vista;
    }



    /* ***************************  ZOOM ****************************************** */


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Toast.makeText(getActivity(),"ZOOM dow",Toast.LENGTH_SHORT).show();
        Log.i("A", "001");
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Toast.makeText(getActivity(),"ZOOM dow",Toast.LENGTH_SHORT).show();
        Log.i("A", "001");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Toast.makeText(getActivity(),"ZOOM dow",Toast.LENGTH_SHORT).show();
        Log.i("A", "001");

    }


    /* ***************************  ZOOM ****************************************** */



    /* ***************************  METODOS  ********************************************* */

    //para unir el borde de llas dos celdas
    ColorDrawable colorIzquierda = new ColorDrawable(Color.BLACK);
    ColorDrawable colorArriba = new ColorDrawable(Color.BLACK);
    ColorDrawable colorAbajo = new ColorDrawable(Color.BLACK);
    ColorDrawable colorDerecha = new ColorDrawable(Color.BLACK);
    ColorDrawable colorFondo = new ColorDrawable(Color.WHITE);
    Drawable[] DrawableArray = new Drawable[]{
            colorIzquierda,
            colorArriba,
            colorAbajo,
            colorDerecha,
            colorFondo
    };
    LayerDrawable layerdrawableEnd,layerdrawableStar;




    // METODO PARA EL TABLELAYAUT DE LA HOJA MENSUAL
    private void TableLayoutDinamicoHojaMensual() {
        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Crear la conexion con la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        //consultar el numero de registros que hay en la db
        int intNumeroTotalDeRegistros;
        try{
            //Cuenta el numero de registros de todos los reg. que sean el año mayor y el mes mayor
            String queryCuentaReg = queryCuentaRegistrosMayorYearMes;
            String stNumeroDeRegistros = DatabaseUtils.stringForQuery(db, queryCuentaReg,null);
            intNumeroTotalDeRegistros = Integer.parseInt(stNumeroDeRegistros);
            //guardar el numero de registro para la multiseleción
            stNumeroRegMultiSelect = stNumeroDeRegistros;
        }catch(NumberFormatException e) {
            intNumeroTotalDeRegistros = 0;
        }
        //crear una consulta
        String queryTodosLosDatosYearMes = queryTodosRegMayorYearMes + " ORDER BY " + Utilidades.CAMPO_NUM_DIA + " ASC ";
        //Crear el cursor que se le pasa la consulta y guarda los datos de la consulta
        Cursor cursor = db.rawQuery(queryTodosLosDatosYearMes,null);
        //contar el numero de columnas que hay en la db
        int numeroDeColumnsaDB = cursor.getColumnCount();
        //variables para el nombre de la columna
        String strID, strFecha,strOtrasActi,strOtrasActiRetri,strDia,strHoraIni,strHoraFin,strTotalHoras,strADR,
                strDesayuno,strComida,strCena,strDormir,strTotalDiets,strExtras;
        //un bucle para recorrer todas las columnas
        for(int i = 0; i < numeroDeColumnsaDB; i++){
            //
            strID = cursor.getColumnName(i);
            strFecha = cursor.getColumnName(i);
            strOtrasActi = cursor.getColumnName(i);
            strOtrasActiRetri = cursor.getColumnName(i);
            strDia = cursor.getColumnName(i);
            strHoraIni = cursor.getColumnName(i);
            strHoraFin = cursor.getColumnName(i);
            strTotalHoras = cursor.getColumnName(i);
            strADR = cursor.getColumnName(i);
            strDesayuno = cursor.getColumnName(i);
            strComida = cursor.getColumnName(i);
            strCena = cursor.getColumnName(i);
            strDormir = cursor.getColumnName(i);
            strTotalDiets = cursor.getColumnName(i);
            strExtras = cursor.getColumnName(i);
            if (strID.equals("id")){intID = cursor.getColumnIndex(strID);}
            if (strFecha.equals("fecha")){intFecha = cursor.getColumnIndex(strFecha);}
            if(strOtrasActi.equals("otrasActividades")){intOtrasActi = cursor.getColumnIndex(strOtrasActi);}
            if(strOtrasActiRetri.equals("retribuido")){intOtrasActiRetri = cursor.getColumnIndex(strOtrasActiRetri);}
            if (strDia.equals("dia")){intDia = cursor.getColumnIndex(strDia);}
            if (strHoraIni.equals("horaIni")){intHoraIni = cursor.getColumnIndex(strHoraIni);}
            if (strHoraFin.equals("horaFin")){intHoraFin = cursor.getColumnIndex(strHoraFin);}
            if (strTotalHoras.equals("totalHoras")){intTotalHoras = cursor.getColumnIndex(strTotalHoras);}
            if (strADR.equals("adr")){intADR = cursor.getColumnIndex(strADR);}
            if (strDesayuno.equals("desayuno")){intDesayuno = cursor.getColumnIndex(strDesayuno);}
            if (strComida.equals("comida")){intComida = cursor.getColumnIndex(strComida);}
            if (strCena.equals("cena")){intCena = cursor.getColumnIndex(strCena);}
            if (strDormir.equals("dormir")){intDormir = cursor.getColumnIndex(strDormir);}
            if (strTotalDiets.equals("totalDietas")){intTotalDiets = cursor.getColumnIndex(strTotalDiets);}
            if (strExtras.equals("extras")){intExtras = cursor.getColumnIndex(strExtras);}

        }


        //variables para los coloares
        String stColrnegro= "#000000";
        String stColrGris = "#a9a0a0";
        String stColrRojo = "#d61411";
        float floTamanoLetra = 24;

        trCabecera.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        //crear los parametros de los textView
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.span = 2;
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.span = 1;




        //Comprobar que el cursor no esté vacio
        if(cursor != null){
            cursor.moveToFirst();
            //El for para recorrer la db
            for(int i = 0; i < intNumeroTotalDeRegistros; i++){

                int x = 3;
                int c = 0;

                //variable que recoge el nombre de la otras actividades
                String strOtraActividad = cursor.getString(intOtrasActi);
                if (strOtraActividad == null) {
                    strOtraActividad = "";
                }
                //en el caso de que sea un dato de otras Actividades se colorea de gris
                if(strOtraActividad.equals("")){stColrnegro = "#000000";}else { stColrnegro= "#a9a0a0";}

                /*
                Parámetros
                índice
                int: el índice del dibujable para ajustar
                l
                int: número de píxeles para agregar al límite izquierdo
                t
                int: número de píxeles para agregar al límite superior
                r
                int: número de píxeles para restar del límite derecho
                b
                int: número de píxeles para restar del límite inferior
                */

                layerdrawableEnd = new LayerDrawable(DrawableArray);
                layerdrawableEnd.setLayerInset(0,c,c,c,c);///////colorIzquierda negre    olor.RED);
                layerdrawableEnd.setLayerInset(1,c,x,c,c);//colorArriba negre    r.BLAK);
                layerdrawableEnd.setLayerInset(2,c,c,c,c);//colorAbajo negre    .GREEN);
                layerdrawableEnd.setLayerInset(3,c,c,c,c);//////colorDerecha blanco    or.BLUE);
                layerdrawableEnd.setLayerInset(4,x,x,c,x);//colorFondo blanco    .YELLOW);

                layerdrawableStar = new LayerDrawable(DrawableArray);
                layerdrawableStar.setLayerInset(0,c,c,c,c);//colorIzquierda
                layerdrawableStar.setLayerInset(1,c,x,c,c);//colorArriba
                layerdrawableStar.setLayerInset(2,c,c,c,c);//colorAbajo
                layerdrawableStar.setLayerInset(3,c,c,c,c);//colorDerecha
                layerdrawableStar.setLayerInset(4,c,x,x,x);//colorFondo


                //La cabecera
                trCabecera = new TableRow(getActivity());
                //Los TextView que hacen de filas
                tvTablaId         = new TextView(getActivity());
                tvTablaFecha      = new TextView(getActivity());
                tvTablaDia        = new TextView(getActivity());
                tvTablaHoraIni    = new TextView(getActivity());
                tvTablaHoraFin    = new TextView(getActivity());
                tvTablaTotalHoras = new TextView(getActivity());
                tvTablaSeparador1 = new TextView(getActivity());
                tvTablaSeparador2 = new TextView(getActivity());
                tvTablaADR        = new TextView(getActivity());
                tvTablaDesayuno   = new TextView(getActivity());
                tvTablaComida     = new TextView(getActivity());
                tvTablaCena       = new TextView(getActivity());
                tvTablaDormir     = new TextView(getActivity());
                tvTablaTotalDieta = new TextView(getActivity());
                tvTablaExtra      = new TextView(getActivity());
                //Los parametros, que son obligatorio, sino sale un error
                /** El contenido de cada celda **/

                //el id
                tvTablaId.setText(cursor.getString(intID));
                tvTablaId.setPadding(5,2,5,2);
                tvTablaId.setTextColor(Color.parseColor(stColrnegro));
                tvTablaId.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaId.setBackgroundResource(R.drawable.disenyo_bordes_4);
                //cuando se vuelve atras, hay que volve a decirle que reinicie los colores.
                GradientDrawable drawable = (GradientDrawable)  tvTablaId.getBackground();
                drawable.setColor(Color.WHITE);
                tvTablaId.setTextSize(floTamanoLetra);
                tvTablaId.setVisibility(View.GONE);

                //La fecha
                tvTablaFecha.setText(cursor.getString(intFecha));
                tvTablaFecha.setPadding(5,2,5,2);
                tvTablaFecha.setTextColor(Color.parseColor(stColrnegro));
                tvTablaFecha.setTextSize(floTamanoLetra);
                tvTablaFecha.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaFecha.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaFecha.isClickable();
                //para remarcar el registro en caso de que ya aya uno seleccionado con un click corto
                tvTablaFecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vCapturaDatosTabla = v;
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaFecha.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //El nombre del dia de la semana
                tvTablaDia.setText(cursor.getString(intDia));
                tvTablaDia.setPadding(5,2,5,2);
                tvTablaDia.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDia.setTextSize(floTamanoLetra);
                tvTablaDia.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                tvTablaDia.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDia.isClickable();
                //para remarcar el registro en caso de que ya aya uno seleccionado con un click corto
                tvTablaDia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vCapturaDatosTabla = v;
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaDia.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //si el nombre está vacio se rellena la tabla normalmente
                if(strOtraActividad.equals("")){
                    //La hora inicial
                    tvTablaHoraIni.setText(cursor.getString(intHoraIni));
                    tvTablaHoraIni.setPadding(5,2,5,2);
                    tvTablaHoraIni.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraIni.setTextSize(floTamanoLetra);
                    tvTablaHoraIni.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    tvTablaHoraIni.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraIni.isClickable();
                    tvTablaHoraIni.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (booMultiselec) {
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir al metodo se selecionar con el View selecccionado y la id o index
                                seleccionarReg(v,stId);
                            }
                        }
                    });

                    //Pulsación larga
                    tvTablaHoraIni.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //Si ya se pulsó un largo, que no se pueda volver a pulsar
                            if(!booMultiselec){
                                //cambiar el boleano para no repetir la acción de pulsación larga.
                                booMultiselec = true;
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir a mostrar animación
                                activarSeleccion(v,stId);
                            }
                            return true;
                        }
                    });
                    //La hora final
                    tvTablaHoraFin.setText(cursor.getString(intHoraFin));
                    tvTablaHoraFin.setPadding(5,2,5,2);
                    tvTablaHoraFin.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraFin.setTextSize(floTamanoLetra);
                    tvTablaHoraFin.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    tvTablaHoraFin.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraFin.isClickable();
                    tvTablaHoraFin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (booMultiselec) {
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir al metodo se selecionar con el View selecccionado y la id o index
                                seleccionarReg(v,stId);
                            }
                        }
                    });

                    //Pulsación larga
                    tvTablaHoraFin.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //Si ya se pulsó un largo, que no se pueda volver a pulsar
                            if(!booMultiselec){
                                //cambiar el boleano para no repetir la acción de pulsación larga.
                                booMultiselec = true;
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir a mostrar animación
                                activarSeleccion(v,stId);
                            }
                            return true;
                        }
                    });

                }
                //si el nombre no está vacio se unen las dos cloumnas y se pone el nombre de la otra actividad
                else{
                    //La hora inicial
                    tvTablaHoraIni.setText(strOtraActividad);
                    tvTablaHoraIni.setPadding(5,2,5,2);
                    tvTablaHoraIni.setTextColor(Color.parseColor(stColrGris));
                    tvTablaHoraIni.setTextSize(floTamanoLetra);
                    tvTablaHoraIni.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    tvTablaHoraIni.isClickable();
                    tvTablaHoraIni.setBackground(layerdrawableEnd);
                   // tvTablaHoraIni.setLayoutParams(params1);
                    tvTablaHoraIni.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (booMultiselec) {
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir al metodo se selecionar con el View selecccionado y la id o index
                                seleccionarReg(v,stId);
                            }
                        }
                    });

                    //Pulsación larga
                    tvTablaHoraIni.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //Si ya se pulsó un largo, que no se pueda volver a pulsar
                            if(!booMultiselec){
                                //cambiar el boleano para no repetir la acción de pulsación larga.
                                booMultiselec = true;
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir a mostrar animación
                                activarSeleccion(v,stId);
                            }
                            return true;
                        }
                    });
                    //La hora final
                    tvTablaHoraFin.setText("");
                    tvTablaHoraFin.setPadding(5,2,5,2);
                    tvTablaHoraFin.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraFin.setTextSize(floTamanoLetra);
                    tvTablaHoraFin.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    tvTablaHoraFin.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraFin.isClickable();
                    tvTablaHoraFin.setBackground(layerdrawableStar);
                    tvTablaHoraFin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (booMultiselec) {
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir al metodo se selecionar con el View selecccionado y la id o index
                                seleccionarReg(v,stId);
                            }
                        }
                    });

                    //Pulsación larga
                    tvTablaHoraFin.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            //Si ya se pulsó un largo, que no se pueda volver a pulsar
                            if(!booMultiselec){
                                //cambiar el boleano para no repetir la acción de pulsación larga.
                                booMultiselec = true;
                                TableRow tablerow = (TableRow) v.getParent();
                                //extraen la id
                                TextView itemsID = (TextView) tablerow.getChildAt(intID);
                                //pasarla a string
                                String stId = itemsID.getText().toString();
                                //ir a mostrar animación
                                activarSeleccion(v,stId);
                            }
                            return true;
                        }
                    });

                }
                //El total de horas trabajadas
                tvTablaTotalHoras.setText(cursor.getString(intTotalHoras));
                tvTablaTotalHoras.setPadding(5,2,5,2);
                tvTablaTotalHoras.setTextColor(Color.parseColor(stColrnegro));
                tvTablaTotalHoras.setTextSize(floTamanoLetra);
                tvTablaTotalHoras.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaTotalHoras.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalHoras.isClickable();
                tvTablaTotalHoras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaTotalHoras.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });
                //El separador 1
                tvTablaSeparador1.setText("");
                tvTablaSeparador1.setPadding(5,2,5,2);

                //El ADR
                tvTablaADR.setText(cursor.getString(intADR));
                tvTablaADR.setPadding(5,2,5,2);
                tvTablaADR.setTextColor(Color.parseColor(stColrnegro));
                tvTablaADR.setTextSize(floTamanoLetra);
                tvTablaADR.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaADR.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaADR.isClickable();
                tvTablaADR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaADR.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //El desayuno
                tvTablaDesayuno.setText(cursor.getString(intDesayuno));
                tvTablaDesayuno.setPadding(5,2,5,2);
                tvTablaDesayuno.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDesayuno.setTextSize(floTamanoLetra);
                tvTablaDesayuno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaDesayuno.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDesayuno.isClickable();
                tvTablaDesayuno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaDesayuno.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //La comida
                tvTablaComida.setText(cursor.getString(intComida));
                tvTablaComida.setPadding(5,2,5,2);
                tvTablaComida.setTextColor(Color.parseColor(stColrnegro));
                tvTablaComida.setTextSize(floTamanoLetra);
                tvTablaComida.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaComida.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaComida.isClickable();
                tvTablaComida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaComida.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //La cena
                tvTablaCena.setText(cursor.getString(intCena));
                tvTablaCena.setPadding(5,2,5,2);
                tvTablaCena.setTextColor(Color.parseColor(stColrnegro));
                tvTablaCena.setTextSize(floTamanoLetra);
                tvTablaCena.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaCena.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaCena.isClickable();
                tvTablaCena.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaCena.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //La noche
                tvTablaDormir.setText(cursor.getString(intDormir));
                tvTablaDormir.setPadding(5,2,5,2);
                tvTablaDormir.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDormir.setTextSize(floTamanoLetra);
                tvTablaDormir.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaDormir.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDormir.isClickable();
                tvTablaDormir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaDormir.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //El total de dietas
                tvTablaTotalDieta.setText(cursor.getString(intTotalDiets));
                tvTablaTotalDieta.setPadding(5,2,5,2);
                tvTablaTotalDieta.setTextColor(Color.parseColor(stColrnegro));
                tvTablaTotalDieta.setTextSize(floTamanoLetra);
                tvTablaTotalDieta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaTotalDieta.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalDieta.isClickable();
                tvTablaTotalDieta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaTotalDieta.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });

                //El separador 2
                tvTablaSeparador2.setText("");
                tvTablaSeparador2.setPadding(5,2,5,2);

                //Los Extras
                tvTablaExtra.setText(cursor.getString(intExtras));
                tvTablaExtra.setPadding(5,2,5,2);
                tvTablaExtra.setTextColor(Color.parseColor(stColrnegro));
                tvTablaExtra.setTextSize(floTamanoLetra);
                tvTablaExtra.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaExtra.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaExtra.isClickable();
                tvTablaExtra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (booMultiselec) {
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir al metodo se selecionar con el View selecccionado y la id o index
                            seleccionarReg(v,stId);
                        }
                    }
                });

                //Pulsación larga
                tvTablaExtra.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        //Si ya se pulsó un largo, que no se pueda volver a pulsar
                        if(!booMultiselec){
                            //cambiar el boleano para no repetir la acción de pulsación larga.
                            booMultiselec = true;
                            TableRow tablerow = (TableRow) v.getParent();
                            //extraen la id
                            TextView itemsID = (TextView) tablerow.getChildAt(intID);
                            //pasarla a string
                            String stId = itemsID.getText().toString();
                            //ir a mostrar animación
                            activarSeleccion(v,stId);
                        }
                        return true;
                    }
                });



                //se pasan los datos a la cabecera
                trCabecera.addView(tvTablaId);
                trCabecera.addView(tvTablaFecha);
                trCabecera.addView(tvTablaDia);
                trCabecera.addView(tvTablaHoraIni);
                trCabecera.addView(tvTablaHoraFin);
                trCabecera.addView(tvTablaTotalHoras);
                trCabecera.addView(tvTablaSeparador1);
                trCabecera.addView(tvTablaADR);
                trCabecera.addView(tvTablaDesayuno);
                trCabecera.addView(tvTablaComida);
                trCabecera.addView(tvTablaCena);
                trCabecera.addView(tvTablaDormir);
                trCabecera.addView(tvTablaTotalDieta);
                trCabecera.addView(tvTablaSeparador2);
                trCabecera.addView(tvTablaExtra);
                //Se pasan los datos a la tabla
                tbly_hoja_mensual.addView(trCabecera, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                cursor.moveToNext();
            }


            /* ********************* ZONA DE LOS TOTALES *************** */

            //LOS TOTALES
            //Implementar la cabecera
            trCabecera = new TableRow(getActivity());

            //los separadores
            //id
            tvTablaSeparadorTotales = new TextView(getActivity());
            tvTablaSeparadorTotales.setVisibility(View.GONE);
            trCabecera.addView(tvTablaSeparadorTotales);
            //fecha
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //dia
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //hora inicio
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //hora final
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //total horas
            tvTablaTotalHorasMes = new TextView(getActivity());
            tvTablaTotalHorasMes.setText(stAcumuladorDeHoras);
            tvTablaTotalHorasMes.setPadding(5,2,5,2);
            tvTablaTotalHorasMes.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalHorasMes.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalHorasMes.setTextSize(floTamanoLetra);
            tvTablaTotalHorasMes.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalHorasMes);
            //separador 1
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //total de dias con adr
            tvTablaTotalDiasADR = new TextView(getActivity());
            tvTablaTotalDiasADR.setText(stNumeroDeDiasADR);
            tvTablaTotalDiasADR.setPadding(5,2,5,2);
            tvTablaTotalDiasADR.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalDiasADR.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDiasADR.setTextSize(floTamanoLetra);
            tvTablaTotalDiasADR.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalDiasADR);
            //total desayuno
            tvTablaTotalDesayuno = new TextView(getActivity());
            tvTablaTotalDesayuno.setText(stPrivateDesayuno);
            tvTablaTotalDesayuno.setPadding(5,2,5,2);
            tvTablaTotalDesayuno.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalDesayuno.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDesayuno.setTextSize(floTamanoLetra);
            tvTablaTotalDesayuno.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalDesayuno);
            //total Comida
            tvTablaTotalComida = new TextView(getActivity());
            tvTablaTotalComida.setText(stPrivateComida);
            tvTablaTotalComida.setPadding(5,2,5,2);
            tvTablaTotalComida.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalComida.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalComida.setTextSize(floTamanoLetra);
            tvTablaTotalComida.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalComida);
            //total cena
            tvTablaTotalCena = new TextView(getActivity());
            tvTablaTotalCena.setText(stPrivateCena);
            tvTablaTotalCena.setPadding(5,2,5,2);
            tvTablaTotalCena.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalCena.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalCena.setTextSize(floTamanoLetra);
            tvTablaTotalCena.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalCena);
            //total dormir
            tvTablaTotalDormir = new TextView(getActivity());
            tvTablaTotalDormir.setText(stPrivateDormir);
            tvTablaTotalDormir.setPadding(5,2,5,2);
            tvTablaTotalDormir.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalDormir.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDormir.setTextSize(floTamanoLetra);
            tvTablaTotalDormir.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalDormir);
            //total dietas
            tvTablaTotalDieta = new TextView(getActivity());
            tvTablaTotalDieta.setText(stPrivateTotalDietas);
            tvTablaTotalDieta.setPadding(5,2,5,2);
            tvTablaTotalDieta.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalDieta.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDieta.setTextSize(floTamanoLetra);
            tvTablaTotalDieta.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaTotalDieta);
            //separador 2
            tvTablaSeparadorTotales = new TextView(getActivity());
            trCabecera.addView(tvTablaSeparadorTotales);
            //total extras
            tvTablaExtraTotal = new TextView(getActivity());
            tvTablaExtraTotal.setText(stPrivateExtras);
            tvTablaExtraTotal.setPadding(5,2,5,2);
            tvTablaExtraTotal.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaExtraTotal.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaExtraTotal.setTextSize(floTamanoLetra);
            tvTablaExtraTotal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabecera.addView(tvTablaExtraTotal);

            tbly_hoja_mensual.addView(trCabecera, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            //metodo para comprobar la disponibilidas
            comprobarDisponibilidad(stAcumuladorDeHoras, stPrivateExtras);

            if(booDisponivilidadVisible){
                trCabecera = new TableRow(getActivity());
                //separadore para la disponivilidad
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                //texto disponivilidad
                tvTablaSeparadorTotales = new TextView(getActivity());
                String stDisponibilidad = "Disponibilidad: ";
                tvTablaSeparadorTotales.setText(stDisponibilidad);
                tvTablaSeparadorTotales.setTextSize(floTamanoLetra - 5);
                tvTablaSeparadorTotales.setPadding(5,2,5,2);
                tvTablaSeparadorTotales.setTextColor(Color.parseColor(stColrnegro));//Color negro
                trCabecera.addView(tvTablaSeparadorTotales);

                //para la disponibilidad
                tvTablaTotalHorasMes = new TextView(getActivity());
                tvTablaTotalHorasMes.setText(stPrivateDisponivilidad);
                tvTablaTotalHorasMes.setTextSize(floTamanoLetra);
                tvTablaTotalHorasMes.setPadding(5,2,5,2);
                tvTablaTotalHorasMes.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
                tvTablaTotalHorasMes.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalHorasMes.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                trCabecera.addView(tvTablaTotalHorasMes);

                //separadore para la disponivilidad
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);
                tvTablaSeparadorTotales = new TextView(getActivity());
                trCabecera.addView(tvTablaSeparadorTotales);

                //para el total de extras más la disponibilidad
                tvTablaExtraTotal = new TextView(getActivity());
                tvTablaExtraTotal.setText(stPrivateTotalExtras);
                tvTablaExtraTotal.setTextSize(floTamanoLetra);
                tvTablaExtraTotal.setPadding(5,2,5,2);
                tvTablaExtraTotal.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
                tvTablaExtraTotal.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaExtraTotal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                trCabecera.addView(tvTablaExtraTotal);

                tbly_hoja_mensual.addView(trCabecera, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
            cursor.close();
        }
            db.close();

        //ETIQUETAS MULTISELECCIÓN.
        lyMenuBorrarMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //esto es nulo pero necesario para que no se marque el registro que queda por debajo de este layout.
            }
        });
        //setOnClick para cancelar todos lo marcado
        tvCancelarSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //metodo para desmarcar todos lo marcado
                desamarcarTodo();
            }
        });
        //onClick para marcar/desmarcar todo
        ivCheckSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobar si está marcado o no
                if(!booCheckSelect){
                    //Ir al metodo para marcarlo todos
                    marcarTodo();
                    booCheckSelect = true;
                    ivCheckSelect.setImageResource(R.drawable.ic_check_select_nada);
                } else {
                    //ir la metodo para desmarcarlo todos
                    desamarcarTodo();
                    booCheckSelect = false;
                    ivCheckSelect.setImageResource(R.drawable.ic_check_select_todo);
                }
                //metodo para marcar todos los registros

            }
        });
        //onClick para borra la selección
        ivBorrarSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para borrar todos lo seleccionado
                borrarSeleccion();
            }
        });
    }


    //metodo para desmarcar todos los registros
    @SuppressLint("ResourceType")
    private void desamarcarTodo() {
        //inflar la tabla
        TableLayout tableLayout = tbly_hoja_mensual;
        //Número de registros en la tabla +1 para el último registro,
        // ya que se inicializa en el for en 1 para saltar la cabecera
        int intNumeroRegistrosTabla = Integer.parseInt(stNumeroRegMultiSelect)+1;
        //bucle para recorre todas las celdas de la tabla inicializado en 1 para salta la cabecera
        for(int iFila=1; iFila<intNumeroRegistrosTabla; iFila++){
            //Para recorrer la fila seleccionada y recorre luego las columnas de dicha fila
            TableRow tabla = (TableRow) tableLayout.getChildAt(iFila);//elige la fila 3 hacia abajo
            //Recorre las columnas de la fila con el numero de columnas validas
            for (int arrColumna : arrColumnas) {
                //creado el textview con numero de la columna según el bucle
                TextView textView = (TextView) tabla.getChildAt(arrColumna);//elige la columna 0 hacia la derecha
                //el diseño de los bordes
                textView.setBackgroundResource(R.drawable.disenyo_bordes_4);
                //para volver a colorear el fondo
                GradientDrawable gradientDrawable = (GradientDrawable) textView.getBackground();
                //el color del fondo
                gradientDrawable.setColor(Color.WHITE);
                //para comprobar si la hora final está vacia para poner el color de la letra en gris
                TextView tvCompColum4 = (TextView) tabla.getChildAt(4);
                //Pasar el valor a una variable String.
                String stCompColum4 = tvCompColum4.getText().toString();
                //Comparacion si la variable está vacia.
                if (stCompColum4.equals("")) {
                    //Si está vacia, se le pasa el color gris.
                    textView.setTextColor(Color.parseColor("#a9a0a0"));
                } else {
                    //Si no está vacia, se le pasa el color negro.
                    textView.setTextColor(Color.BLACK);
                }
            }
        }
        //Se esconde el layout multiselección.
        animacion(false);
    }

    //Metodo para marcarlo todos.
    @SuppressLint("SetTextI18n")
    private void marcarTodo(){
        //Inflar la tabla.
        TableLayout tableLayout = tbly_hoja_mensual;
        //Número de registros en la tabla +1 para el último registro,
        //ya que se inicializa en el for en 1 para saltar la cabecera
        int intNumeroRegistrosTabla = Integer.parseInt(stNumeroRegMultiSelect)+1; //bucle para recorre todas las celdas de la tabla inicializado en 1 para salta la cabecera
        for(int iFila=1; iFila<intNumeroRegistrosTabla; iFila++) {
            //Para recorrer la fila seleccionada y recorre luego las columnas de dicha fila
            TableRow tabla = (TableRow) tableLayout.getChildAt(iFila);//elige la fila  hacia abajo
            //extraer el indice
            TextView tvIndice = (TextView) tabla.getChildAt(0);
            //pasar el indice a una variable
            String stIndice = tvIndice.getText().toString();
            //si el indiece no está en el arraylist, lo guarda.
            if(!alStRegSelect.contains(stIndice)){
                //meter el indice en el arrayList
                alStRegSelect.add(stIndice);
                intContadorRegMultiSelect++;
            }

            //Recorre las columnas de la fila con el numero de columnas validas
            for (int arrColumna : arrColumnas) {
                //creado el textview con numero de la columna según el bucle
                TextView textView = (TextView) tabla.getChildAt(arrColumna);//elige la columna  hacia la derecha
                //el diseño de los bordes
                textView.setBackgroundResource(R.drawable.disenyo_bordes_4);
                //para volver a colorear el fondo
                GradientDrawable gradientDrawable = (GradientDrawable) textView.getBackground();
                //el color de fondo
                gradientDrawable.setColor(Color.parseColor("#7480D1"));
                //el color del texto
                textView.setTextColor(Color.WHITE);
            }
        }
        //se muestra el numero de registros selecionados y el total de registro que hay en el mes.
        tvContadorSelect.setText(intContadorRegMultiSelect+ "/"+stNumeroRegMultiSelect);
    }

    //metodo que activa la multiseleccion marcando el primer registro con pulsacion larga
    @SuppressLint("SetTextI18n")
    private void activarSeleccion(View v,String stId) {
        //cuando se hace la pulsación larga, que cambia el color de fondo y el color de la letra y se
        //activa el marcar o desmarcar con una pulsación corta

        //Mostrar el menu de borrar la selección
        lyMenuBorrarMultiple.setVisibility(View.VISIBLE);
        //se marcan los bordes de la celda
        v.setBackgroundResource(R.drawable.disenyo_bordes_4);
        //Se llama a la tabla
        TableRow tablerow = (TableRow) v.getParent();
        //try para capturar un posible error y que no se detenga la app
        try{
            //un bucle que recorra los indices del array
            for (int i=0;i<12;i++){
                //se crea un textView que es la celda corresponiente al chil + el id
                TextView itemsGenerico = (TextView) tablerow.getChildAt(arrColumnas[i]);
                //Se le pasa el color del texto.
                itemsGenerico.setTextColor(Color.WHITE);
                //Se le pasa un setBackGrounResource por si lleba el "layerdrawableEnd" dá error
                itemsGenerico.setBackgroundResource(R.drawable.disenyo_bordes_4);
                //
                GradientDrawable drawableGenerico = (GradientDrawable) itemsGenerico.getBackground();
                //El color de fondo.
                drawableGenerico.setColor(Color.parseColor("#7480D1"));
            }
        }catch (Exception e){
            Toast.makeText(getActivity(),"Error ArchivoConsultasVerDia"+e   ,Toast.LENGTH_SHORT).show();
        }
        //almacenar id en el arraylist
        alStRegSelect.add(stId);


        /** setBackgroundResource y setBackgroundColor utilizan internamente
         *la misma API setBackgroundDrawable para realizar sus tareas.
         *Entonces uno sobrescribe al otro. Por lo tanto, no podrá lograr
         *su objetivo con esta API.*/

        //contador que muestr el numero de registros seleccionados
        intContadorRegMultiSelect = intContadorRegMultiSelect + 1;
        //se muestra el numero de registros selecionados y el total de registro que hay en el mes.
        tvContadorSelect.setText(intContadorRegMultiSelect+ "/"+stNumeroRegMultiSelect);
        //poner en marcha la animacion
        animacion(true);

    }


    //marca/desmarca registros con pulsación corta.
    @SuppressLint("SetTextI18n")
    private void seleccionarReg(View v,String stId) {

        //Comprobar si existe el registr
        if(alStRegSelect.contains(stId)){//SI EXISTE
            //se desmarca y se boora el registro del arraylist
            alStRegSelect.remove(stId);
            //se marcan los bordes de la celda
            v.setBackgroundResource(R.drawable.disenyo_bordes_4);
            //Se llama a la tabla
            TableRow tablerow = (TableRow) v.getParent();
            //se crea una array con los indices de la tabla
            int[] arrColumnas = {1,2,3,4,5,7,8,9,10,11,12,14};
            //try para capturar un posible error y que no se detenga la app
            try{
                //un bucle que recorra los indices del array
                for (int i=0;i<12;i++){
                    //se crea un textView que es la celda corresponiente al chil + el id
                    TextView itemsGenerico = (TextView) tablerow.getChildAt(arrColumnas[i]);
                    //Se le pasa un setBackGrounResource por si lleba el "layerdrawableEnd" dá error
                    itemsGenerico.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    //
                    GradientDrawable drawableGenerico = (GradientDrawable) itemsGenerico.getBackground();
                    //El color de fondo.
                    drawableGenerico.setColor(Color.parseColor("#ffffff"));
                    //extraer el texto de la hora inicial para ver si está vacio
                    TextView tvCompColum4 = (TextView) tablerow.getChildAt(4);
                    String stCompColum4 = tvCompColum4.getText().toString();
                    //comprobar si la hora fina eata vacia para poner la fila en gris o negro
                    if(stCompColum4.equals("")){
                        //El color del texto si no hay horas.
                        itemsGenerico.setTextColor(Color.parseColor("#a9a0a0"));
                    }
                    else {
                        //Se le pasa el color del texto si tiene horas.
                        itemsGenerico.setTextColor(Color.BLACK);
                    }
                }
                //se le resta un registro a la info del layout
                intContadorRegMultiSelect--;
                //se le cambia la imagen de la multiseleción
                ivCheckSelect.setImageResource(R.drawable.ic_check_select_todo);
                //se le pasa a false la seleccion de todos los registros
                booCheckSelect = false;
            }catch (Exception e){
                Toast.makeText(getActivity(),"Error ArchivoConsultasVerDia"+e   ,Toast.LENGTH_SHORT).show();
            }
        }
        else {//NO EXISTE
            //almacenar id en el arraylist
            alStRegSelect.add(stId);
            //marcar el registro
            //se marcan los bordes de la celda
            v.setBackgroundResource(R.drawable.disenyo_bordes_4);
            //Se llama a la tabla
            TableRow tablerow = (TableRow) v.getParent();
            //se crea una array con los indices de la tabla
            int[] arrColumnas = {1,2,3,4,5,7,8,9,10,11,12,14};
            //try para capturar un posible error y que no se detenga la app
            try{
                //un bucle que recorra los indices del array
                for (int i=0;i<12;i++){
                    //se crea un textView que es la celda corresponiente al chil + el id
                    TextView itemsGenerico = (TextView) tablerow.getChildAt(arrColumnas[i]);
                    //Se le pasa el color del texto.
                    itemsGenerico.setTextColor(Color.WHITE);
                    //Se le pasa un setBackGrounResource por si lleba el "layerdrawableEnd" dá error
                    itemsGenerico.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    //
                    GradientDrawable drawableGenerico = (GradientDrawable) itemsGenerico.getBackground();
                    //El color de fondo.
                    drawableGenerico.setColor(Color.parseColor("#7480D1"));
                }
                //se le suma un registro a la info del layout
                intContadorRegMultiSelect++;
            }catch (Exception e){
                Toast.makeText(getActivity(),"Error ArchivoConsultasVerDia"+e   ,Toast.LENGTH_SHORT).show();
            }
        }



        //Si se deseleccionan todos los registros se oculta el layout selecciono multiple
        if(alStRegSelect.size() == 0){
            animacion(false);
        }

        //se muestra el numero de registros selecionados y el total de registro que hay en el mes.
        tvContadorSelect.setText(intContadorRegMultiSelect+ "/"+stNumeroRegMultiSelect);
    }


    private void animacion(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if(mostrar){
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,1.0f,
                    Animation.RELATIVE_TO_SELF,0.0f);
            animation.setDuration(500);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set,0.1f);
            lyMenuBorrarMultiple.setLayoutAnimation(controller);
            lyMenuBorrarMultiple.startAnimation(animation);
        }
        else {
            //ocultar el menu de borrar la selección si se desmarcan todas
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF,1.0f);
            animation.setDuration(250);
            set.addAnimation(animation);
            LayoutAnimationController controller = new LayoutAnimationController(set,0.1f);
            lyMenuBorrarMultiple.setLayoutAnimation(controller);
            lyMenuBorrarMultiple.startAnimation(animation);
            lyMenuBorrarMultiple.setVisibility(View.GONE);

            //desactivar la multiseleccion con un click corto
            booMultiselec = true;
            booMultiselec = false;
            alStRegSelect.clear();
            intContadorRegMultiSelect = 0;
            ivCheckSelect.setImageResource(R.drawable.ic_check_select_todo);
            booCheckSelect=false;
        }
    }
    //Metodo  para borra la selección
    private void borrarSeleccion(){
        //
        preguntarBorrarDiaSelect();
    }
    //Metodo que calcula si hay disponivilidad
    private void comprobarDisponibilidad(String AcomuladorDeHoras, String PrivateExtras) {
        //sacar las horas del mes
        String[] stArrayHorasMes = AcomuladorDeHoras.split(":");
        //pasarlo a una variable convertido en numero
        int intHorasMes = Integer.parseInt(stArrayHorasMes[0]);
        double douHorasExtras = Double.parseDouble(PrivateExtras.replace(",","."));
        //comparar si son menos o más del computo de 160 horas mensuales
        if(intHorasMes > 160){
            //Si hay más hors de 160, se le restan para ver cuanstas hay de más
            int intResult = intHorasMes - 160;
            //un bucle para mostras las horas de mas
            //de 1 a 15 = 90
            if(intResult >= 1 && intResult <= 15){
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 90)).replace(".",",");
            }
            //de 16 a 30 = 180
            if(intResult >= 16 && intResult <= 30){
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 180)).replace(".",",");
            }
            //de 30 a 45 = 270
            if(intResult >= 30 && intResult <= 45){
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 270)).replace(".",",");
            }
            //de 46 a 60 = 360
            if(intResult >= 46 && intResult <= 60){
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 360)).replace(".",",");
            }
            //a partir de 60 = 450
            if(intResult >61){
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 450)).replace(".",",");
            }
            booDisponivilidadVisible = true;

        }
        else{

            booDisponivilidadVisible = false;
        }


    }

    //Metodo para sumar las horas del mes
    private void sumarHorasDelMes(String hora1) {

        //otra hora. hay que pasarla a minutos
        String stHora2 = stAcumuladorDeHoras;
        //un array para separar las horas de los minutos
        String[] arraySepararHora1 = hora1.split(":");
        String[] arraySepararHora2 = stHora2.split(":");
        //pasar las horas a minutos y sumarlos
        double douSumaHoasMinutos1 = Double.parseDouble(arraySepararHora1[0])*60 + Double.parseDouble(arraySepararHora1[1]);
        double douSumaHoasMinutos2 = Double.parseDouble(arraySepararHora2[0])*60 + Double.parseDouble(arraySepararHora2[1]);
        //sumar todos los minutos y pasarlos a horas con un resto decimal de minutos
        double douMinutosSumadosPasadosEnHoras = (douSumaHoasMinutos1 + douSumaHoasMinutos2)/60;
        //estraer las horas enteras
        int intExtraerHorasEnteras = (int)douMinutosSumadosPasadosEnHoras;
        //dejar los decimales de los minutos sin las horas
        double douSoloDecimalesMinutos = douMinutosSumadosPasadosEnHoras - intExtraerHorasEnteras;
        //pasar los decimales a horas
        double douPasarMinutosEnHoras = douSoloDecimalesMinutos*60;
        //redondear las horas
        double douRedondeoHoras = Math.round(douPasarMinutosEnHoras);
        //eliminar decimales
        int intMinutosSinDecimales = (int)douRedondeoHoras;
        //pasar las horas a string
        String stHora = (intExtraerHorasEnteras < 10)? "0" + (intExtraerHorasEnteras) : String.valueOf(intExtraerHorasEnteras);
        //pasar los minutos a string
        String stMinutos = (intMinutosSinDecimales < 10)? "0" + (intMinutosSinDecimales) : String.valueOf(intMinutosSinDecimales);
        //pasar la hora completa a una cadena
        //Pasarlo al acomulador
        stAcumuladorDeHoras = stHora + ":" + stMinutos;


    }


    //Metodo para las consultas a la db
    private void consultasDB(){
        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Crear la conexion con la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        int intNumeroTotalDeRegistros;
        try{
            String queryNumeroDeRegistros = queryCuentaRegistrosMayorYearMes;
            String stNumeroDeRegistros = DatabaseUtils.stringForQuery(db, queryNumeroDeRegistros,null);
            intNumeroTotalDeRegistros = Integer.parseInt(stNumeroDeRegistros);
        }catch(NumberFormatException e) {
            intNumeroTotalDeRegistros = 0;
        }
        try{
            //Variable para la consulta
            //TODAS LAS HORAS
            String queryTotalHorasMes = "SELECT " + Utilidades.CAMPO_TOTAL_HORAS + " FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + " WHERE "
                    + Utilidades.CAMPO_NUM_YEAR + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR
                    + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + ") AND "
                    +  Utilidades.CAMPO_NUM_MES + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_MES
                    + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + " WHERE " + Utilidades.CAMPO_NUM_YEAR +
                    " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + "))";
            //ADR
            String queryADR = queryCuentaRegistrosMayorYearMes + " AND " + Utilidades.CAMPO_ADR + " = \"si\"";
            //DIETAS Y EXTRAS
            String queryDietas = "SELECT " + Utilidades.CAMPO_DESAYUNO + ","
                                            + Utilidades.CAMPO_COMIDA + ","
                                            + Utilidades.CAMPO_CENA + ","
                                            + Utilidades.CAMPO_DORMIR + ","
                                            + Utilidades.CAMPO_TOTAL_DIETAS + ","
                                            + Utilidades.CAMPO_EXTRAS + " FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + " WHERE "
                    + Utilidades.CAMPO_NUM_YEAR + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR
                    + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + ") AND "
                    +  Utilidades.CAMPO_NUM_MES + " = (SELECT MAX(" + Utilidades.CAMPO_NUM_MES
                    + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + " WHERE " + Utilidades.CAMPO_NUM_YEAR +
                    " = (SELECT MAX(" + Utilidades.CAMPO_NUM_YEAR + ") FROM " + Utilidades.TABLA_FICHAMENSUAL + "))";

            //pasar la consulta y recoger el dato.
            stNumeroDeDiasADR = DatabaseUtils.stringForQuery(db,queryADR,null);
            Cursor cursorTotalHorasMes = db.rawQuery(queryTotalHorasMes,null);
            Cursor cursorDietas = db.rawQuery(queryDietas,null);

            //comprobar que elcursor no está vacio // total horas del mes
            if(cursorTotalHorasMes != null){
                //Mover el cursor al primer registro
                cursorTotalHorasMes.moveToFirst();
                //bucle para recorrer todos los registros
                for(int i = 0; i < intNumeroTotalDeRegistros; i++) {
                    //pasar a un string la hora de la db
                    String stHora1 = cursorTotalHorasMes.getString(0);
                    //pasar la hora al metodo de sumar horas
                    sumarHorasDelMes(stHora1);
                    cursorTotalHorasMes.moveToNext();
                }
                cursorTotalHorasMes.close();
            }

            //Sumar dietas y extras
            if(cursorDietas != null){
                //Mover el cursor a la primera posición
                cursorDietas.moveToFirst();
                //bucle para recorrer todos los registros
                for(int i = 0; i < intNumeroTotalDeRegistros; i++) {
                    //pasar a un string el desayuno de la db
                    String stDesayuno = cursorDietas.getString(0);
                    if(stDesayuno == null){ stDesayuno = "0";}
                    String stComida = cursorDietas.getString(1);
                    if(stComida == null){ stComida = "0";}
                    String stCena = cursorDietas.getString(2);
                    if(stCena == null){ stCena = "0";}
                    String stDormir = cursorDietas.getString(3);
                    if(stDormir == null){ stDormir = "0";}
                    String stTotalDietas = cursorDietas.getString(4);
                    if(stTotalDietas == null){ stTotalDietas = "0";}
                    String stExtras = cursorDietas.getString(5);
                    if(stExtras == null || stExtras.equals("")){ stExtras = "0";}
                    //pasar precio al metodo de sumar dietas
                    sumarDieta(stDesayuno,stComida,stCena,stDormir,stTotalDietas,stExtras);
                    cursorDietas.moveToNext();
                }
                cursorDietas.close();
            }

        }
        catch (NumberFormatException ignored){

        }

        //cerrar la base de datos
        db.close();


    }

    //metodo para sumar todas las dietas y los extras
    private void sumarDieta(String stDesayuno, String stComida, String stCena, String stDormir, String stTotalDietas, String stExtras) {
        //pasar los datos a int
        double douDesayuno = Double.parseDouble(stDesayuno.replace(",","."));
        double douComida= Double.parseDouble(stComida.replace(",","."));
        double douCena = Double.parseDouble(stCena.replace(",","."));
        double douDormir = Double.parseDouble(stDormir.replace(",","."));
        double douTotalDietas = Double.parseDouble(stTotalDietas.replace(",","."));
        double douExtras = Double.parseDouble(stExtras.replace(",","."));
        //pasar lo acomulado a int
        double douAcumuladorDesayuno = Double.parseDouble(stPrivateDesayuno.replace(",","."));
        double douAcumuladorComida = Double.parseDouble(stPrivateComida.replace(",","."));
        double douAcumuladorCena = Double.parseDouble(stPrivateCena.replace(",","."));
        double douAcumuladorDormir = Double.parseDouble(stPrivateDormir.replace(",","."));
        double douAcumuladorTotalDietas = Double.parseDouble(stPrivateTotalDietas.replace(",","."));
        double douAcumuladorExtras = Double.parseDouble(stPrivateExtras.replace(",","."));
        //sumar las dietas y pasarlas al acumulador
            stPrivateDesayuno = String.valueOf(df.format(douDesayuno + douAcumuladorDesayuno)).replace(".",",");
        stPrivateComida = String.valueOf(df.format(douComida+ douAcumuladorComida)).replace(".",",");
        stPrivateCena = String.valueOf(df.format(douCena + douAcumuladorCena)).replace(".",",");
        stPrivateDormir = String.valueOf(df.format(douDormir + douAcumuladorDormir)).replace(".",",");
        stPrivateTotalDietas = String.valueOf(df.format(douTotalDietas + douAcumuladorTotalDietas)).replace(".",",");
        stPrivateExtras = String.valueOf(df.format(douExtras + douAcumuladorExtras)).replace(".",",");
    }


    //para borrar el dia seleccionado a traves del id
    private void preguntarBorrarDiaSelect() {
        //mortra un dialogo de alerta
        //El titiulo del alertDialog
        String stTitulo = "¡¡¡ ATENCIÓN !!!";
        //el mensaje
        String stMsg = "Estas a punto de borrar  " + alStRegSelect.size() + " registros\n¿Estas Seguro?\nUna vez borrados no se podrán recuperar";
        //boton positivo
        String stPositivo = "SI";
        //boton negatibo
        String stNegativo = "NO";

        AlertDialog.Builder alertaBorrado = new AlertDialog.Builder(getActivity());
        alertaBorrado.setMessage(stMsg)
                .setTitle(stTitulo)
                .setPositiveButton(stPositivo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        booBorrarDia = true;
                        borrarElDiaSelect();
                    }
                })
                .setNegativeButton(stNegativo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "No se a borrado nada. ", Toast.LENGTH_SHORT).show();
                                booBorrarDia = false;
                            }
                        }
                );
        alertaBorrado.show();

    }
    //para borrar el dia seleccionado a traves del id
    private void borrarElDiaSelect() {
        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Abrir la tabla en modo escritura
        SQLiteDatabase bd = conn.getWritableDatabase();
        try {
            //Extraer el número de registros que hay en el array.
            int intNumRegArray = alStRegSelect.size();
            //bucle que recorre los indices a borrar
            for(String alStRegSelect : alStRegSelect ){
                //el texto de la consulta
                String qBorrarDia = "DELETE FROM " + Utilidades.TABLA_FICHAMENSUAL + " WHERE " + Utilidades.CAMPO_ID + " =  \"" + alStRegSelect + "\"";
                bd.execSQL(qBorrarDia);
            }
            Toast.makeText(getActivity(),"Los " + intNumRegArray + " registros han sido borrados."  ,Toast.LENGTH_SHORT).show();
            int intComprobador = Integer.parseInt(stNumeroRegMultiSelect);
            if(intNumRegArray == intComprobador){
                //volver a inicio
                Fragment fragmentVolveInicio =new Inicio();
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, fragmentVolveInicio);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            //reiniciar los acumuladores
            stAcumuladorDeHoras = "00:00";
            //
            stNumeroDeDiasADR = "0" ;
            //
            stPrivateDesayuno = "0";
            stPrivateComida = "0";
            stPrivateCena = "0";
            stPrivateDormir = "0";
            stPrivateTotalDietas = "0";
            //
            stPrivateExtras = "0";
            //
            stPrivateDisponivilidad = "0";
            stPrivateTotalExtras = "0";

            setUserVisibleHint(true);
        } catch (SQLException ignored) {

        }
        bd.close();
        desamarcarTodo();
    }


    /* ***************************  METODOS FIN ****************************************** */



    /* ***************************  ZOOM ****************************************** */




    /* ***************************  ZOOM ****************************************** */

    //para refrescar la actividad
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
        if (!isVisibleToUser) {
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Para que al girar la pantalla no vuelva al inico


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
