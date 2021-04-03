package com.nanduky.controlflix;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nanduky.controlflix.utilidades.Utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArchivoConsultasVerDias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArchivoConsultasVerDias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivoConsultasVerDias extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArchivoConsultasVerDias() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchivoConsultasVerDias.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivoConsultasVerDias newInstance(String param1, String param2) {
        ArchivoConsultasVerDias fragment = new ArchivoConsultasVerDias();
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
    //el tableLayaut para la hoja mensual
    TableLayout tbly_hoja_del_mes_seleccionado;
    //Para las filas de la tabla
    TableRow trCabeceraDelMesSeleccionado;
    //Los textView que hacen de columnas
    TextView tvTablaIdDelMesSeleccionado,
            tvTablaFechaDelMesSeleccionado, tvTablaDiaDelMesSeleccionado,
            tvTablaHoraIniDelMesSeleccionado, tvTablaHoraFinDelMesSeleccionado,
            tvTablaTotalHorasDelMesSeleccionado, tvTablaADRDelMesSeleccionado,
            tvTablaDesayunoDelMesSeleccionado, tvTablaComidaDelMesSeleccionado,
            tvTablaCenaDelMesSeleccionado, tvTablaDormirDelMesSeleccionado,
            tvTablaTotalDietaDelMesSeleccionado, tvTablaExtraDelMesSeleccionado,
            tvTablaSeparador1DelMesSeleccionado, tvTablaSeparador2DelMesSeleccionado;
    //las casillas de los totales
    TextView tvTablaTotalHorasMesDelMesSeleccionado,
            tvTablaTotalDiasADRDelMesSeleccionado, tvTablaTotalDesayunoDelMesSeleccionado,
            tvTablaTotalComidaDelMesSeleccionado, tvTablaTotalCenaDelMesSeleccionado,
            tvTablaTotalDormirDelMesSeleccionado, tvTablaExtraTotalDelMesSeleccionado,
            tvTablaSeparadorTotalesDelMesSeleccionado;

    //boton de volver a meses
    Button btVolverAlMes, btGuardarPDF;

    //Vadiable para ir acumulando el total de horas para el "cursor"
    private String stAcumuladorDeHoras = "00:00";
    //Variable que contendrá el numero de días trabajados con adr
    private String stNumeroDeDiasADR = "0";
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
    //variables para recoger el año y el mes del que vienen de aechivoConsulta
    private String stMesDelQueViene = "", stYearDelQueViene = "";
    //variable para recoger la id del dia seleccionado para el borrado
    private String stIDdiaSelect = "";


    //variables para guardar el numero de index de la columna
    private int intID = 0, intFecha = 0,intOtrasActi=0, intOtrasActiRetri = 0, intDia = 0, intHoraIni = 0, intHoraFin = 0, intTotalHoras = 0, intADR = 0,
            intDesayuno = 0, intComida = 0, intCena = 0, intDormir = 0, intTotalDiets = 0, intExtras = 0;

    //boleano para borrar o no el dia seleccionado
    Boolean booBorrarDia = null;

    //variables para generar y guardar los pdf's
    private static final String NOMBRE_CARPETA_PDF = "TransFlix";
    private static final String GENERADOS = "MisArchivos";
    //Variable para guardar las cabeceras de la tabla.
    private String[] arrayCeldas;

    /* ************* zoma multiseleccion ************* */

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

    /* ************* zoma multiseleccion ************* */


    /* ****************************   NUEVOS ACCEOS FIN  ***************************************** */


    /* *********************************   CONSULTORIO   ******************************************/

    /**
     * Crear conexión con la base de datos
     **/

    //el nombre de la base de datos será db_fichaMensual
    ConexionSQliteHelper conn = new ConexionSQliteHelper(getActivity(), "db_fichaMensual", null, versionDb);

    //CONSULTAS DB
    //Todos los registros del ultimo mes del ultimo año
    String qTodoDelMesYearSelect = "";
    //Cuenta los registros del el ultimo mes del aña mas grande
    String qCuentaRegistrosDelMesYearSelect = "";
    /* *********************************   CONSULTORIO  FIN ***************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_archivo_consultas_ver_dias, container, false);

        //el tableLayaut de la hoja mensual
        tbly_hoja_del_mes_seleccionado = vista.findViewById(R.id.tbly_hoja_del_mes_seleccionado);
        //la fila cabecera
        trCabeceraDelMesSeleccionado = vista.findViewById(R.id.trCabeceraDelMesSeleccionado);
        //las columnas
        tvTablaIdDelMesSeleccionado = vista.findViewById(R.id.tvTablaidDelMesSeleccionado);
        tvTablaFechaDelMesSeleccionado = vista.findViewById(R.id.tvTablaFechaDelMesSeleccionado);
        tvTablaDiaDelMesSeleccionado = vista.findViewById(R.id.tvTablaDiaDelMesSeleccionado);
        tvTablaHoraIniDelMesSeleccionado = vista.findViewById(R.id.tvTablaHoraIniDelMesSeleccionado);
        tvTablaHoraFinDelMesSeleccionado = vista.findViewById(R.id.tvTablaHoraFinDelMesSeleccionado);
        tvTablaTotalHorasDelMesSeleccionado = vista.findViewById(R.id.tvTablaTotalHoras);
        tvTablaADRDelMesSeleccionado = vista.findViewById(R.id.tvTablaADRDelMesSeleccionado);
        tvTablaDesayunoDelMesSeleccionado = vista.findViewById(R.id.tvTablaDesayunoDelMesSeleccionado);
        tvTablaComidaDelMesSeleccionado = vista.findViewById(R.id.tvTablaComidaDelMesSeleccionado);
        tvTablaCenaDelMesSeleccionado = vista.findViewById(R.id.tvTablaCenaDelMesSeleccionado);
        tvTablaDormirDelMesSeleccionado = vista.findViewById(R.id.tvTablaDormirDelMesSeleccionado);
        tvTablaTotalDietaDelMesSeleccionado = vista.findViewById(R.id.tvTablaTotalDietaDelMesSeleccionado);
        tvTablaExtraDelMesSeleccionado = vista.findViewById(R.id.tvTablaExtraDelMesSeleccionado);
        tvTablaSeparador1DelMesSeleccionado = vista.findViewById(R.id.tvTablaSeparador1DelMesSeleccionado);
        tvTablaSeparador2DelMesSeleccionado = vista.findViewById(R.id.tvTablaSeparador2DelMesSeleccionado);
        //boton volvealmes
        btVolverAlMes = vista.findViewById(R.id.btVolverAlMes);
        //para el boton de generar el pdf
        btGuardarPDF = vista.findViewById(R.id.bt_guardar_pdf);
        //LinealLayout para el menú de borrar
        lyMenuBorrarMultiple = vista.findViewById(R.id.lyMenuBorrarMultiple);
        tvCancelarSelect = vista.findViewById(R.id.tvCancelarSelect);
        tvContadorSelect = vista.findViewById(R.id.tvContadorSelect);
        ivCheckSelect = vista.findViewById(R.id.ivCheckSelect);
        ivBorrarSelect = vista.findViewById(R.id.ivBorrarSelect);


        /* ***************************  ON CREATE  ********************************************* */

        //cuando se gira la pantalla se vuelve a cargar los datos del mes y año
        if(savedInstanceState != null){
            //variables que reciben el Bundel que se está creando para mandarlo
            stMesDelQueViene = savedInstanceState.getString("Mes_Seleccionado");
            stYearDelQueViene = savedInstanceState.getString("Year_Seleccionado");
            //se crea el Bundel
            Bundle bunRecogerMesYear = new Bundle();
            //Se le pasan los datos para la fecha
            bunRecogerMesYear.putString("stMesSelect",stMesDelQueViene);
            bunRecogerMesYear.putString("stYearSelect",stYearDelQueViene);
            //Se crea el Fragme nuevo para recargar este fragment
            Fragment fragmentArchivoConsultaVerDias = new ArchivoConsultasVerDias();
            //se le pasan los datos al fragment y se le dice que "recargue" la paguina
            fragmentArchivoConsultaVerDias.setArguments(bunRecogerMesYear);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragmentArchivoConsultaVerDias);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        //recoger el mes y el año seleccionado en ArchivoConsulta
        Bundle bunRecogerMesYear = this.getArguments();
        //comprobar que no venga vacio
        if (bunRecogerMesYear != null) {
            //meter en una variable en nombre del mes
            String stMesDelQueVieneParaConvertir = bunRecogerMesYear.getString("stMesSelect");
            //Convertir el nombre del mes en numero.
            switch (stMesDelQueVieneParaConvertir) {
                case "enero":
                case "01":
                    stMesDelQueViene = "01";
                    break;
                case "febrero":
                case "02":
                    stMesDelQueViene = "02";
                    break;
                case "marzo":
                case "03":
                    stMesDelQueViene = "03";
                    break;
                case "abril":
                case "04":
                    stMesDelQueViene = "04";
                    break;
                case "mayo":
                case "05":
                    stMesDelQueViene = "05";
                    break;
                case "junio":
                case "06":
                    stMesDelQueViene = "06";
                    break;
                case "julio":
                case "07":
                    stMesDelQueViene = "07";
                    break;
                case "agosto":
                case "08":
                    stMesDelQueViene = "08";
                    break;
                case "septiembre":
                case "09":
                    stMesDelQueViene = "09";
                    break;
                case "octubre":
                case "10":
                    stMesDelQueViene = "10";
                    break;
                case "noviembre":
                case "11":
                    stMesDelQueViene = "11";
                    break;
                case "diciembre":
                case "12":
                    stMesDelQueViene = "12";
                    break;
                default:
                    break;
            }
            //meter en una variable en numero del año
            stYearDelQueViene = bunRecogerMesYear.getString("stYearSelect");

        }


        //Llama al metodo que cuenta/consulta que cuenta los dias de adr
        consultasDB();
        //Llamar al metodo encargado de construir la tabla dinamica.
        TableLayoutDinamicoConsultasVerDia();


        //el boton de volover a los mese
        btVolverAlMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metodo para volver atras
                volverAlMes();

            }
        });

        btGuardarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPDF();
            }
        });

        /* ***************************  ON CREATE FIN ****************************************** */
        return vista;

    }

    /* *********************************  METODOS  *************************************** */


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

    //variable para guardar la ruta y el nombre del pdf
    private String nombre_completo = "";
    //variable boleana para saber si se pasó por la petición de serie
    boolean yaPreguntado = false;

    //metodo para generar el pdf
    private void generarPDF() {

        //variable para guardar el nombre del mes
        String stNombreDelMes = "";
        //bucle para extrer el nombre del mes
        switch (stMesDelQueViene) {
            case "01": stNombreDelMes = "enero"; break;
            case "02": stNombreDelMes = "febrero"; break;
            case "03": stNombreDelMes = "marzo"; break;
            case "04": stNombreDelMes = "abril"; break;
            case "05": stNombreDelMes = "mayo"; break;
            case "06": stNombreDelMes = "junio"; break;
            case "07": stNombreDelMes = "julio"; break;
            case "08": stNombreDelMes = "agosto"; break;
            case "09": stNombreDelMes = "septiembre"; break;
            case "10": stNombreDelMes = "octubre"; break;
            case "11": stNombreDelMes = "noviembre"; break;
            case "12": stNombreDelMes = "diciembre"; break;

        }
        //Primero comprobamos si hay permisos de escritora y lectura
        if (validarPermisos()) {
            //crear ruta de guardado
            String destinoPDF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
            //pasar a un File la ruta y el nombre del archivo de destino
            File fileDestino = new File(destinoPDF + File.separator + NOMBRE_CARPETA_PDF + File.separator
                    + stYearDelQueViene);
            //El nombre del archivo
            String NOMBRE_ARCHIVO = stMesDelQueViene + " " + stNombreDelMes + " " + stYearDelQueViene + ".pdf";

            //un boleano para comprobar si existe la ruta
            boolean siExiste = fileDestino.exists();
            //se comprueba si existe el directorio
            if (!siExiste) {
                //si no existe, se crea uno nuevo
                siExiste = fileDestino.mkdirs();
            }
            //el nombre completo
            nombre_completo = fileDestino + File.separator + NOMBRE_ARCHIVO;
            //pasarlo a un File como ruta completa
            final File outputfile = new File(nombre_completo);
            //comprobar si existe el archivo
            if (outputfile.exists()) {
                dialogoYaExiste();
            }
            //si no existe, se crea
            else {
                crearDocumento();
            }
        } else {
            if (yaPreguntado) {
                //solicitar los permisos de forma manual
                solicitarPermisoaManual();
            }
        }
    }

    //metodo para comprobar si hay permisos de escritura
    private boolean validarPermisos() {
        //comprobar la version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        //comprobar los permisos de escritura
        if ((ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        //si no hay permisos, hay que pedirlos
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //metodo que cargará un dialogo donde recomendará que acepte los permisos
            cargarDialogoRecomendacion();
        } else {
            //esto cargará directamente la solicitud de permiso
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length != 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //solicitar los permisos de forma manual
                yaPreguntado = true;
                solicitarPermisoaManual();
            } else {
                //
                yaPreguntado = true;
            }
        }
    }

    String pack = "";

    //metodo par pedir los permisos de forma manual.
    private void solicitarPermisoaManual() {
        try {
            pack = Objects.requireNonNull(getActivity()).getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //crear las opciones a mostrar
        final CharSequence[] opciones = {"Si", "NO"};
        //crear el dialogo de alerta
        final AlertDialog.Builder buiAceptarManual = new AlertDialog.Builder(getActivity());
        //el titulo
        buiAceptarManual.setTitle("¿Quieres activar los permisos de forma manual?");
        //el boton que se pulsará
        buiAceptarManual.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", pack, null);
                    intent.setData(uri);
                    startActivity(intent);

                } else {
                    dialog.dismiss();
                }
            }
        });
        buiAceptarManual.show();
    }

    //metodo para cargar u un dialogo para que acepten los permisos de escritura
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder buiAceptarPermisos = new AlertDialog.Builder(getActivity());
        buiAceptarPermisos.setTitle("Permisos desactivados");
        buiAceptarPermisos.setMessage("Debes aceptar los permisos para poder generar el PDF");
        buiAceptarPermisos.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });
        buiAceptarPermisos.show();
    }

    //metodo para avisar de que el fichero ya existe
    private void dialogoYaExiste() {
        //se advierte que ya existe y se le dá opción de guradar o no
        AlertDialog.Builder buiYaExiste = new AlertDialog.Builder(getActivity());
        buiYaExiste.setTitle("¡¡¡ ATENCIÓN !!!");
        buiYaExiste.setMessage("El pdf de este mes ya fue creado\n¿Quieres sobrescribirlo");
        buiYaExiste.setPositiveButton("si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sobrescribirPDF();
                crearDocumento();
            }
        });
        buiYaExiste.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        buiYaExiste.show();
    }

    //metodo para borrar el pdf viejo
    private void sobrescribirPDF() {
        final File outputfile = new File(nombre_completo);
        //se borra el archivo
        boolean borrarpdf = outputfile.delete();

    }

    //metodo que creará el documento
    private void crearDocumento() {
        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn = new ConexionSQliteHelper(getActivity(), "db_fichaMensual", null, versionDb);
        //Crear la conexion con la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        //consultar el numero de registros que hay en la db
        int intNumeroTotalDeRegistros;
        try {
            //Cuenta el numero de registros de todos los reg. que sean el año mayor y el mes mayor
            String queryCuentaReg = qCuentaRegistrosDelMesYearSelect;
            String stNumeroDeRegistros = DatabaseUtils.stringForQuery(db, queryCuentaReg, null);
            intNumeroTotalDeRegistros = Integer.parseInt(stNumeroDeRegistros);
        } catch (NumberFormatException e) {
            intNumeroTotalDeRegistros = 0;
        }
        //crear una consulta
        String queryTodosLosDatosYearMes = qTodoDelMesYearSelect;
        //Crear el cursor que se le pasa la consulta y guarda los datos de la consulta
        Cursor cursor = db.rawQuery(queryTodosLosDatosYearMes, null);
        //contar el numero de columnas que hay en la db
        int numeroDeColumnsaDB = cursor.getColumnCount();
        //variables para el nombre de la columna
        String strID, strFecha,strOtrasActi,strOtrasActiRetri, strDia, strHoraIni, strHoraFin, strTotalHoras, strADR,
                strDesayuno, strComida, strCena, strDormir, strTotalDiets, strExtras;
        //un bucle para recorrer todas las columnas y recojer el index de cada columna
        arrayCeldas = new String[numeroDeColumnsaDB];
        for (int i = 0; i < numeroDeColumnsaDB; i++) {
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
            if (strID.equals("id")){
                intID = cursor.getColumnIndex(strID);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strFecha.equals("fecha")){
                intFecha = cursor.getColumnIndex(strFecha);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strOtrasActi.equals("otrasActividades")){
                intOtrasActi = cursor.getColumnIndex(strOtrasActi);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strOtrasActiRetri.equals("retribuido")){
                intOtrasActiRetri = cursor.getColumnIndex(strOtrasActiRetri);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strDia.equals("dia")){
                intDia = cursor.getColumnIndex(strDia);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strHoraIni.equals("horaIni")){
                intHoraIni = cursor.getColumnIndex(strHoraIni);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strHoraFin.equals("horaFin")){
                intHoraFin = cursor.getColumnIndex(strHoraFin);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strTotalHoras.equals("totalHoras")){
                intTotalHoras = cursor.getColumnIndex(strTotalHoras);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strADR.equals("adr")){
                intADR = cursor.getColumnIndex(strADR);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strDesayuno.equals("desayuno")){
                intDesayuno = cursor.getColumnIndex(strDesayuno);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strComida.equals("comida")){
                intComida = cursor.getColumnIndex(strComida);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strCena.equals("cena")){
                intCena = cursor.getColumnIndex(strCena);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strDormir.equals("dormir")){
                intDormir = cursor.getColumnIndex(strDormir);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strTotalDiets.equals("totalDietas")){
                intTotalDiets = cursor.getColumnIndex(strTotalDiets);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
            if (strExtras.equals("extras")){
                intExtras = cursor.getColumnIndex(strExtras);arrayCeldas[i] = String.valueOf(cursor.getColumnName(i));}
        }

        try {
            //se crea un nuevo documento
            Document document = new Document();
            //se le dá el formato que tendrá el archivo
            document.setPageSize(PageSize.A4.rotate());
            //poner nombre al documento
            PdfWriter.getInstance(document, new FileOutputStream(nombre_completo)).setInitialLeading(0);
            //se abre el documentop
            float leftMargin = 0, rigthMargin = 0, topMargin = 0, bottonMargin = 0;
            document.open();
            //el tituto y otro metadatos
            document.addTitle("TransFlix_PDF");
            document.addSubject("PDF del mes elegido");
            document.addAuthor("Fernando Piñón");
            document.addCreator("Nanduky");
            document.setMargins(leftMargin, rigthMargin, topMargin, bottonMargin);
            document.addCreationDate();

            //el tipo de fuente que se  va a usar
            Font fuenteCabecera = FontFactory.getFont(FontFactory.HELVETICA, 11f, Font.BOLDITALIC, BaseColor.WHITE);
            Font fuenteDisponibilidad= FontFactory.getFont(FontFactory.HELVETICA, 9f, Font.BOLDITALIC, BaseColor.BLACK);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK);
            Font fuenteOtrasActi = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.LIGHT_GRAY);
            Font fuenteTotalesDietasSuetas = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.LIGHT_GRAY);
            Font fuenteTotales = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.RED);




            //se crea la tabla
            float fecha = 8.5f,dia = 8f,hini = 11f,hfin = 11f,totalh = 12.5f,espacio = 1f,adr = 5f,desay = 10f,
                    comida = 9f,cena = 7f,dormir = 9f,totaldoetafecha = 7f,extra = 8f;
            float[] anchoCell = new float[]{fecha, dia, hini, hfin, totalh,  espacio, adr, desay,
                    comida, cena, dormir,  totaldoetafecha, espacio, extra};
            String[] arrayCabecera = {"FECHA","DIA","HORA INICIO","HORA FINAL","TOTAL HORAS"," ",
                    "ADR","DESAYUNO","COMIDA","CENA","DORMIR","TOTAL"," ","EXTRAS" };
            PdfPTable pTable = new PdfPTable(anchoCell);
            //se le pasan las cabeceras
            pTable.setHorizontalAlignment(0);
            PdfPCell colunmCabecera = new PdfPCell();
            //pTable.setWidths(anchoCell);
            pTable.setWidthPercentage(100);

            //se rellena la fila de la cabecera
            for (String s : arrayCabecera) {
                Paragraph datoPara = new Paragraph(s, fuenteCabecera);
                PdfPCell pCell = new PdfPCell();
                pCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pCell.setPaddingBottom(5f);
                pCell.setPaddingLeft(3f);
                pCell.addElement(datoPara);
                String stVacio = " ";
                if(s.equals(stVacio)){
                    pCell.setBackgroundColor(BaseColor.WHITE);
                    pCell.setBorder(Rectangle.NO_BORDER);
                }
                else {
                    pCell.setBackgroundColor(BaseColor.BLUE);
                }
                pCell.setBorderColor(new BaseColor(225,225,225));
                pTable.addCell(pCell);
               // pTable.addCell(new PdfPCell(new Phrase(s, fuenteCabecera)));

            }
            //mover el cursor al principio
            cursor.moveToFirst();
            //recorre la db para llenar la tabla
            for(int i = 0; i < intNumeroTotalDeRegistros; i++){


                //variable que recoge el nombre de la otras actividades
                String strOtraActividad = cursor.getString(intOtrasActi);
                if (strOtraActividad == null) {strOtraActividad = "";}
                //si es un dato de otras actividades, se le cambia el color.
                if (strOtraActividad.equals("")) {
                    fuenteNormal = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.BLACK);
                }else fuenteNormal =  new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD, BaseColor.LIGHT_GRAY);


                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intFecha), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intDia), fuenteNormal))).setPaddingLeft(3f);

                //si el nombre está vacio se rellena la tabla normalmente       .setBorder(Rectangle.NO_BORDER)
                if (strOtraActividad.equals("")) {
                    pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intHoraIni), fuenteNormal))).setPaddingLeft(3f);
                    pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intHoraFin), fuenteNormal))).setPaddingLeft(3f);
                } else {
                    pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intOtrasActi),
                            fuenteOtrasActi))).setColspan(2);
                    pTable.addCell(new PdfPCell(new Phrase("",
                            fuenteNormal))).setBorder(Rectangle.NO_BORDER|Rectangle.BOTTOM|Rectangle.TOP|Rectangle.RIGHT);

                }

                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intTotalHoras), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase("", fuenteNormal))).setBorder(Rectangle.NO_BORDER);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intADR), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intDesayuno), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intComida), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intCena), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intDormir), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase(cursor.getString(intTotalDiets), fuenteNormal))).setPaddingLeft(3f);
                pTable.addCell(new PdfPCell(new Phrase("", fuenteNormal))).setBorder(Rectangle.NO_BORDER);
                //si el extra es cero pero se le borra el 0,00
                String stValorExtra = cursor.getString(intExtras);
                //si es null sale error, pues le damos un valor vacio
                if (stValorExtra == null) {stValorExtra = "";}
                //si es cero, se vacia el string
                if (stValorExtra.equals("0,00")) {stValorExtra = "";}

                pTable.addCell(new PdfPCell(new Phrase(stValorExtra, fuenteNormal))).setPaddingLeft(3f);

                cursor.moveToNext();
            }
            //y los totales

            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//fecha
            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//dia
            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//hora inicio
            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//hora final
            pTable.addCell(new PdfPCell(new Phrase(stAcumuladorDeHoras, fuenteTotales))).setPaddingLeft(3f);//total horas
            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//separador 1
            pTable.addCell(new PdfPCell(new Phrase(stNumeroDeDiasADR, fuenteTotales))).setPaddingLeft(3f);//total de dias con adr
            pTable.addCell(new PdfPCell(new Phrase(stPrivateDesayuno, fuenteTotalesDietasSuetas))).setPaddingLeft(3f);//total desayuno
            pTable.addCell(new PdfPCell(new Phrase(stPrivateComida, fuenteTotalesDietasSuetas))).setPaddingLeft(3f);//total Comida
            pTable.addCell(new PdfPCell(new Phrase(stPrivateCena, fuenteTotalesDietasSuetas))).setPaddingLeft(3f);//total cena
            pTable.addCell(new PdfPCell(new Phrase(stPrivateDormir, fuenteTotalesDietasSuetas))).setPaddingLeft(3f);//total dormir
            pTable.addCell(new PdfPCell(new Phrase(stPrivateTotalDietas, fuenteTotales))).setPaddingLeft(3f);//total dietas
            pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//separador 2
            pTable.addCell(new PdfPCell(new Phrase(stPrivateExtras, fuenteTotales))).setPaddingLeft(3f);//total extras


            if (booDisponivilidadVisible) {
                pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//fecha
                pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//dia
                pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//hora inicio
                pTable.addCell(new PdfPCell(new Phrase("Disponibilidad: ", fuenteDisponibilidad))).setPaddingLeft(3f);//hora final
                pTable.addCell(new PdfPCell(new Phrase(stPrivateDisponivilidad, fuenteTotales))).setPaddingLeft(3f);//total horas
                pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//separador 1
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotales))).setBorder(Rectangle.NO_BORDER);//total de dias con adr
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotalesDietasSuetas))).setBorder(Rectangle.NO_BORDER);//total desayuno
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotalesDietasSuetas))).setBorder(Rectangle.NO_BORDER);//total Comida
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotalesDietasSuetas))).setBorder(Rectangle.NO_BORDER);//total cena
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotalesDietasSuetas))).setBorder(Rectangle.NO_BORDER);//total dormir
                pTable.addCell(new PdfPCell(new Phrase("", fuenteTotales))).setBorder(Rectangle.NO_BORDER);//total dietas
                pTable.addCell(new PdfPCell(new Phrase("", fuenteCabecera))).setBorder(Rectangle.NO_BORDER);//separador 2
                pTable.addCell(new PdfPCell(new Phrase(stPrivateTotalExtras, fuenteTotales))).setPaddingLeft(3f);//total extras

            }


            document.add(pTable);


            document.close();
            db.close();
            Toast.makeText(getActivity(), "documento generado", Toast.LENGTH_SHORT).show();
            Context contexto = getActivity();
            muestraPDF(nombre_completo, contexto);
        } catch (DocumentException e) {
            Toast.makeText(getActivity(), "error A (linea 784 ArchivoConsultasVerDias)", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "Error B (linea 786 ArchivoConsultasVerDias)", Toast.LENGTH_SHORT).show();
            String errorB = e.getMessage();
            examinarError(errorB);

        }
        cursor.close();
    }

    private void examinarError(String errorB) {
        //mortra un dialogo de alerta
        //El titiulo del alertDialog
        String stTitulo = "¡¡¡ ATENCIÓN !!!";
        //el mensaje
        String stMsg = "El error es: \n" + errorB + "\n (linea 799 ArchivoConsultasVerDias)";
        //boton positivo
        String stPositivo = "Cerrar";

        AlertDialog.Builder alertaBorrado = new AlertDialog.Builder(getActivity());
        alertaBorrado.setMessage(stMsg)
                .setTitle(stTitulo)
                .setPositiveButton(stPositivo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertaBorrado.show();
    }

    //metodo para abrir el pdf una vez generado
    public void muestraPDF(String archivo, Context context) {
        //se le pasa el la ruto y el nombre completo al file
        File file = new File(archivo);
        //Se crea el intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //se cierra el archivo abierto(no funciona)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //se piden permisos (no hacen falta)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //se cierra el anterior
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //se inicia un nuevo archivo
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //comprobar version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //crear authorities
            String authorities = Objects.requireNonNull(getActivity()).getPackageName() + ".provider";
            Uri uriPDF = FileProvider.getUriForFile(getActivity(), authorities, file);
            intent.setDataAndType(uriPDF, "application/pdf");

        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }

        try {
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No hay instalada una aplicación para abrir PDF", Toast.LENGTH_SHORT).show();
        }
    }


    // METODO PARA EL TABLELAYAUT DE LA HOJA MENSUAL
    private void TableLayoutDinamicoConsultasVerDia() {
        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn = new ConexionSQliteHelper(getActivity(), "db_fichaMensual", null, versionDb);
        //Crear la conexion con la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        //consultar el numero de registros que hay en la db
        int intNumeroTotalDeRegistros;
        try {
            //Cuenta el numero de registros de todos los reg. que sean el año mayor y el mes mayor
            String queryCuentaReg = qCuentaRegistrosDelMesYearSelect;
            String stNumeroDeRegistros = DatabaseUtils.stringForQuery(db, queryCuentaReg, null);
            intNumeroTotalDeRegistros = Integer.parseInt(stNumeroDeRegistros);
            //guardar el numero de registro para la multiseleción
            stNumeroRegMultiSelect = stNumeroDeRegistros;
        } catch (NumberFormatException e) {
            intNumeroTotalDeRegistros = 0;
        }
        //crear una consulta
        String queryTodosLosDatosYearMes = qTodoDelMesYearSelect;
        //Crear el cursor que se le pasa la consulta y guarda los datos de la consulta
        final Cursor cursor = db.rawQuery(queryTodosLosDatosYearMes, null);
        //contar el numero de columnas que hay en la db
        int numeroDeColumnsaDB = cursor.getColumnCount();
        //variables para el nombre de la columna
        String strID, strFecha,strOtrasActi,strOtrasActiRetri, strDia, strHoraIni, strHoraFin, strTotalHoras, strADR,
                strDesayuno, strComida, strCena, strDormir, strTotalDiets, strExtras;

        //un bucle para recorrer todas las columnas y recojer el index de cada columna
        for (int i = 0; i < numeroDeColumnsaDB; i++) {
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
            if (strID.equals("id")) {
                intID = cursor.getColumnIndex(strID);
            }
            if (strFecha.equals("fecha")) {
                intFecha = cursor.getColumnIndex(strFecha);
            }
            if(strOtrasActi.equals("otrasActividades")){intOtrasActi = cursor.getColumnIndex(strOtrasActi);}
            if(strOtrasActiRetri.equals("retribuido")){intOtrasActiRetri = cursor.getColumnIndex(strOtrasActiRetri);}
            if (strDia.equals("dia")) {
                intDia = cursor.getColumnIndex(strDia);
            }
            if (strHoraIni.equals("horaIni")) {
                intHoraIni = cursor.getColumnIndex(strHoraIni);
            }
            if (strHoraFin.equals("horaFin")) {
                intHoraFin = cursor.getColumnIndex(strHoraFin);
            }
            if (strTotalHoras.equals("totalHoras")) {
                intTotalHoras = cursor.getColumnIndex(strTotalHoras);
            }
            if (strADR.equals("adr")) {
                intADR = cursor.getColumnIndex(strADR);
            }
            if (strDesayuno.equals("desayuno")) {
                intDesayuno = cursor.getColumnIndex(strDesayuno);
            }
            if (strComida.equals("comida")) {
                intComida = cursor.getColumnIndex(strComida);
            }
            if (strCena.equals("cena")) {
                intCena = cursor.getColumnIndex(strCena);
            }
            if (strDormir.equals("dormir")) {
                intDormir = cursor.getColumnIndex(strDormir);
            }
            if (strTotalDiets.equals("totalDietas")) {
                intTotalDiets = cursor.getColumnIndex(strTotalDiets);
            }
            if (strExtras.equals("extras")) {
                intExtras = cursor.getColumnIndex(strExtras);
            }

        }

        //variables para los coloares
        String stColrnegro = "#000000";
        String stColrGris = "#a9a0a0";
        String stColrRojo = "#d61411";
        //el tamaño de la letra
        float floTamanoLetra = 24;

        //la cabecera el las columnas que hay que crear
        trCabeceraDelMesSeleccionado.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        //Comprobar que el cursor no esté vacio
        if (cursor != null) {
            cursor.moveToFirst();
            //El for para recorrer la db

            for ( int i = 0; i < intNumeroTotalDeRegistros; i++) {


                //variable que recoge el nombre de la otras actividades
                String strOtraActividad = cursor.getString(intOtrasActi);
                //se le cambia el null por un vacio
                if (strOtraActividad == null) {strOtraActividad = "";}
                //se cambia el color del texto en caso de que sea de otra actividad
                if(strOtraActividad.equals("")){stColrnegro = "#000000";}else { stColrnegro= "#a9a0a0";}


                int x = 3;
                int c = 0;


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
                layerdrawableEnd.setLayerInset(0,c,c,c,c);///////colorIzquierda
                layerdrawableEnd.setLayerInset(1,c,x,c,c);//colorArriba
                layerdrawableEnd.setLayerInset(2,c,c,c,c);//colorAbajo
                layerdrawableEnd.setLayerInset(3,c,c,c,c);//////colorDerecha
                layerdrawableEnd.setLayerInset(4,x,x,c,x);//colorFondo

                layerdrawableStar = new LayerDrawable(DrawableArray);
                layerdrawableStar.setLayerInset(0,c,c,c,c);//colorIzquierda
                layerdrawableStar.setLayerInset(1,c,x,c,c);//colorArriba
                layerdrawableStar.setLayerInset(2,c,c,c,c);//colorAbajo
                layerdrawableStar.setLayerInset(3,c,c,c,c);//colorDerecha
                layerdrawableStar.setLayerInset(4,c,x,x,x);//colorFondo

                //La cabecera
                trCabeceraDelMesSeleccionado = new TableRow(getActivity());
                //Los TextView que hacen de filas
                tvTablaIdDelMesSeleccionado = new TextView(getActivity());
                tvTablaFechaDelMesSeleccionado = new TextView(getActivity());
                tvTablaDiaDelMesSeleccionado = new TextView(getActivity());
                tvTablaHoraIniDelMesSeleccionado = new TextView(getActivity());
                tvTablaHoraFinDelMesSeleccionado = new TextView(getActivity());
                tvTablaTotalHorasDelMesSeleccionado = new TextView(getActivity());
                tvTablaSeparador1DelMesSeleccionado = new TextView(getActivity());
                tvTablaSeparador2DelMesSeleccionado = new TextView(getActivity());
                tvTablaADRDelMesSeleccionado = new TextView(getActivity());
                tvTablaDesayunoDelMesSeleccionado = new TextView(getActivity());
                tvTablaComidaDelMesSeleccionado = new TextView(getActivity());
                tvTablaCenaDelMesSeleccionado = new TextView(getActivity());
                tvTablaDormirDelMesSeleccionado = new TextView(getActivity());
                tvTablaTotalDietaDelMesSeleccionado = new TextView(getActivity());
                tvTablaExtraDelMesSeleccionado = new TextView(getActivity());
                //Los parametros, que son obligatorio, sino sale un error
                /** El contenido de cada celda **/  //tvTablaIdDelMesSeleccionado
                //el id
                tvTablaIdDelMesSeleccionado.setText(cursor.getString(intID));
                tvTablaIdDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaIdDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaIdDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaIdDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                //cuando se vuelve atras, hay que volve a decirle que reinicie los colores.
                GradientDrawable drawable = (GradientDrawable)  tvTablaIdDelMesSeleccionado.getBackground();
                drawable.setColor(Color.WHITE);
                tvTablaIdDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaIdDelMesSeleccionado.setVisibility(View.GONE);



                //La fecha
                tvTablaFechaDelMesSeleccionado.setText(cursor.getString(intFecha));
                tvTablaFechaDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaFechaDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaFechaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaFechaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaFechaDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaFechaDelMesSeleccionado.isClickable();
                //para remarcar el registro en caso de que ya aya uno seleccionado con un click corto
                tvTablaFechaDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaFechaDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaDiaDelMesSeleccionado.setText(cursor.getString(intDia));
                tvTablaDiaDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaDiaDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDiaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDiaDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaDiaDelMesSeleccionado.isClickable();
                tvTablaDiaDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaDiaDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                    tvTablaHoraIniDelMesSeleccionado.setText(cursor.getString(intHoraIni));
                    tvTablaHoraIniDelMesSeleccionado.setPadding(5,2,5,2);
                    tvTablaHoraIniDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraIniDelMesSeleccionado.setTextSize(floTamanoLetra);
                    tvTablaHoraIniDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    tvTablaHoraIniDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraIniDelMesSeleccionado.isClickable();
                    tvTablaHoraIniDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                    tvTablaHoraIniDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                    tvTablaHoraFinDelMesSeleccionado.setText(cursor.getString(intHoraFin));
                    tvTablaHoraFinDelMesSeleccionado.setPadding(5,2,5,2);
                    tvTablaHoraFinDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraFinDelMesSeleccionado.setTextSize(floTamanoLetra);
                    tvTablaHoraFinDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    tvTablaHoraFinDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraFinDelMesSeleccionado.isClickable();
                    tvTablaHoraFinDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                    tvTablaHoraFinDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                    tvTablaHoraIniDelMesSeleccionado.setText(strOtraActividad);
                    tvTablaHoraIniDelMesSeleccionado.setPadding(5,2,5,2);
                    tvTablaHoraIniDelMesSeleccionado.setTextColor(Color.parseColor(stColrGris));
                    tvTablaHoraIniDelMesSeleccionado.setTextSize(floTamanoLetra);
                    tvTablaHoraIniDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    tvTablaHoraIniDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraIniDelMesSeleccionado.isClickable();
                    tvTablaHoraIniDelMesSeleccionado.setBackground(layerdrawableEnd);
                    // tvTablaHoraIni.setLayoutParams(params1);
                    tvTablaHoraIniDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                    tvTablaHoraIniDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                    tvTablaHoraFinDelMesSeleccionado.setText("");
                    tvTablaHoraFinDelMesSeleccionado.setPadding(5,2,5,2);
                    tvTablaHoraFinDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                    tvTablaHoraFinDelMesSeleccionado.setTextSize(floTamanoLetra);
                    tvTablaHoraFinDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    tvTablaHoraFinDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                    tvTablaHoraFinDelMesSeleccionado.isClickable();
                    tvTablaHoraFinDelMesSeleccionado.setBackground(layerdrawableStar);
                    tvTablaHoraFinDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                    tvTablaHoraFinDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaTotalHorasDelMesSeleccionado.setText(cursor.getString(intTotalHoras));
                tvTablaTotalHorasDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaTotalHorasDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaTotalHorasDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaTotalHorasDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalHorasDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaTotalHorasDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaTotalHorasDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaSeparador1DelMesSeleccionado.setText("");
                tvTablaSeparador1DelMesSeleccionado.setPadding(5, 2, 5, 2);



                //El ADR
                tvTablaADRDelMesSeleccionado.setText(cursor.getString(intADR));
                tvTablaADRDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaADRDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaADRDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaADRDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaADRDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaADRDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaADRDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaDesayunoDelMesSeleccionado.setText(cursor.getString(intDesayuno));
                tvTablaDesayunoDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaDesayunoDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDesayunoDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaDesayunoDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDesayunoDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaDesayunoDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaDesayunoDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaComidaDelMesSeleccionado.setText(cursor.getString(intComida));
                tvTablaComidaDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaComidaDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaComidaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaComidaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaComidaDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaComidaDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaComidaDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaCenaDelMesSeleccionado.setText(cursor.getString(intCena));
                tvTablaCenaDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaCenaDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaCenaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaCenaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaCenaDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaCenaDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaCenaDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaDormirDelMesSeleccionado.setText(cursor.getString(intDormir));
                tvTablaDormirDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaDormirDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaDormirDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaDormirDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaDormirDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaDormirDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaDormirDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaTotalDietaDelMesSeleccionado.setText(cursor.getString(intTotalDiets));
                tvTablaTotalDietaDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaTotalDietaDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaTotalDietaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaTotalDietaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalDietaDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaTotalDietaDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaTotalDietaDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                tvTablaSeparador2DelMesSeleccionado.setText("");
                tvTablaSeparador2DelMesSeleccionado.setPadding(5, 2, 5, 2);




                //Los Extras
                //si el extra es cero pero se le borra el 0,00
                String stValorExtra = cursor.getString(intExtras);
                //si es null sale error, pues le damos un valor vacio
                if (stValorExtra == null) {stValorExtra = "";}
                //si es cero, se vacia el string
                if (stValorExtra.equals("0,00")) {stValorExtra = "";}
                tvTablaExtraDelMesSeleccionado.setText(stValorExtra);
                tvTablaExtraDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaExtraDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));
                tvTablaExtraDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                tvTablaExtraDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaExtraDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaExtraDelMesSeleccionado.setOnClickListener(new View.OnClickListener() {
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
                tvTablaExtraDelMesSeleccionado.setOnLongClickListener(new View.OnLongClickListener() {
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
                trCabeceraDelMesSeleccionado.addView(tvTablaIdDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaFechaDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaDiaDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaHoraIniDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaHoraFinDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaTotalHorasDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparador1DelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaADRDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaDesayunoDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaComidaDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaCenaDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaDormirDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaTotalDietaDelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparador2DelMesSeleccionado);
                trCabeceraDelMesSeleccionado.addView(tvTablaExtraDelMesSeleccionado);
                //Se pasan los datos a la tabla
                tbly_hoja_del_mes_seleccionado.addView(trCabeceraDelMesSeleccionado,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                cursor.moveToNext();
            }


            /* ********************* ZONA DE LOS TOTALES *************** */

            //LOS TOTALES
            //Implementar la cabecera
            trCabeceraDelMesSeleccionado = new TableRow(getActivity());

            //los separadores
            //id
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            tvTablaSeparadorTotalesDelMesSeleccionado.setVisibility(View.GONE);
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //fecha
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //dia
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //hora inicio
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //hora final
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //total horas
            tvTablaTotalHorasMesDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalHorasMesDelMesSeleccionado.setText(stAcumuladorDeHoras);
            tvTablaTotalHorasMesDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalHorasMesDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalHorasMesDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalHorasMesDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalHorasMesDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalHorasMesDelMesSeleccionado);
            //separador 1
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //total de dias con adr
            tvTablaTotalDiasADRDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalDiasADRDelMesSeleccionado.setText(stNumeroDeDiasADR);
            tvTablaTotalDiasADRDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalDiasADRDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalDiasADRDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDiasADRDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalDiasADRDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalDiasADRDelMesSeleccionado);
            //total desayuno
            tvTablaTotalDesayunoDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalDesayunoDelMesSeleccionado.setText(stPrivateDesayuno);
            tvTablaTotalDesayunoDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalDesayunoDelMesSeleccionado.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalDesayunoDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDesayunoDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalDesayunoDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalDesayunoDelMesSeleccionado);
            //total Comida
            tvTablaTotalComidaDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalComidaDelMesSeleccionado.setText(stPrivateComida);
            tvTablaTotalComidaDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalComidaDelMesSeleccionado.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalComidaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalComidaDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalComidaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalComidaDelMesSeleccionado);
            //total cena
            tvTablaTotalCenaDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalCenaDelMesSeleccionado.setText(stPrivateCena);
            tvTablaTotalCenaDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalCenaDelMesSeleccionado.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalCenaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalCenaDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalCenaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalCenaDelMesSeleccionado);
            //total dormir
            tvTablaTotalDormirDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalDormirDelMesSeleccionado.setText(stPrivateDormir);
            tvTablaTotalDormirDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalDormirDelMesSeleccionado.setTextColor(Color.parseColor(stColrGris));//color gris a9a0a0
            tvTablaTotalDormirDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDormirDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalDormirDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalDormirDelMesSeleccionado);
            //total dietas
            tvTablaTotalDietaDelMesSeleccionado = new TextView(getActivity());
            tvTablaTotalDietaDelMesSeleccionado.setText(stPrivateTotalDietas);
            tvTablaTotalDietaDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaTotalDietaDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaTotalDietaDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaTotalDietaDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaTotalDietaDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaTotalDietaDelMesSeleccionado);
            //separador 2
            tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
            trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
            //total extras
            tvTablaExtraTotalDelMesSeleccionado = new TextView(getActivity());
            tvTablaExtraTotalDelMesSeleccionado.setText(stPrivateExtras);
            tvTablaExtraTotalDelMesSeleccionado.setPadding(5, 2, 5, 2);
            tvTablaExtraTotalDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
            tvTablaExtraTotalDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
            tvTablaExtraTotalDelMesSeleccionado.setTextSize(floTamanoLetra);
            tvTablaExtraTotalDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            trCabeceraDelMesSeleccionado.addView(tvTablaExtraTotalDelMesSeleccionado);

            tbly_hoja_del_mes_seleccionado.addView(trCabeceraDelMesSeleccionado,
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

            //metodo para comprobar la disponibilidas
            comprobarDisponibilidadDelMesSeleccionado(stAcumuladorDeHoras, stPrivateExtras);

            if (booDisponivilidadVisible) {
                trCabeceraDelMesSeleccionado = new TableRow(getActivity());
                //separadore para la disponivilidad
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                //texto disponivilidad
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                String stDisponibilidadDelMesSeleccionado = "Disponibilidad: ";
                tvTablaSeparadorTotalesDelMesSeleccionado.setText(stDisponibilidadDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado.setTextSize(floTamanoLetra -5);
                tvTablaSeparadorTotalesDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaSeparadorTotalesDelMesSeleccionado.setTextColor(Color.parseColor(stColrnegro));//Color negro
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);

                //para la disponibilidad
                tvTablaTotalHorasMesDelMesSeleccionado = new TextView(getActivity());
                tvTablaTotalHorasMesDelMesSeleccionado.setText(stPrivateDisponivilidad);
                tvTablaTotalHorasMesDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaTotalHorasMesDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaTotalHorasMesDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
                tvTablaTotalHorasMesDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaTotalHorasMesDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                trCabeceraDelMesSeleccionado.addView(tvTablaTotalHorasMesDelMesSeleccionado);

                //separadore para la disponivilidad
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);
                tvTablaSeparadorTotalesDelMesSeleccionado = new TextView(getActivity());
                trCabeceraDelMesSeleccionado.addView(tvTablaSeparadorTotalesDelMesSeleccionado);

                //para el total de extras más la disponibilidad
                tvTablaExtraTotalDelMesSeleccionado = new TextView(getActivity());
                tvTablaExtraTotalDelMesSeleccionado.setText(stPrivateTotalExtras);
                tvTablaExtraTotalDelMesSeleccionado.setTextSize(floTamanoLetra);
                tvTablaExtraTotalDelMesSeleccionado.setPadding(5, 2, 5, 2);
                tvTablaExtraTotalDelMesSeleccionado.setTextColor(Color.parseColor(stColrRojo));//color rojo d61411
                tvTablaExtraTotalDelMesSeleccionado.setBackgroundResource(R.drawable.disenyo_bordes_4);
                tvTablaExtraTotalDelMesSeleccionado.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                trCabeceraDelMesSeleccionado.addView(tvTablaExtraTotalDelMesSeleccionado);


                tbly_hoja_del_mes_seleccionado.addView(trCabeceraDelMesSeleccionado,
                        new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
            }


            cursor.close();
        }
        db.close();

        lyMenuBorrarMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //esto es nulo pero necesario para que no se marque el registro que queda por debajo de este layout.
            }
        });

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
        TableLayout tableLayout = tbly_hoja_del_mes_seleccionado;
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
        TableLayout tableLayout = tbly_hoja_del_mes_seleccionado;
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


    //
    private void comprobarDisponibilidadDelMesSeleccionado(String AcumuladorDeHoras, String PrivateExtras) {
        //sacar las horas del mes
        String[] stArrayHorasMes = AcumuladorDeHoras.split(":");
        //pasarlo a una variable convertido en numero
        int intHorasMes = Integer.parseInt(stArrayHorasMes[0]);
        double douHorasExtras = Double.parseDouble(PrivateExtras.replace(",", "."));
        //comparar si son menos o más del computo de 160 horas mensuales
        if (intHorasMes > 160) {
            //Si hay más hors de 160, se le restan para ver cuanstas hay de más
            int intResult = intHorasMes - 160;
            //un bucle para mostras las horas de mas
            //de 1 a 15 = 90
            if (intResult >= 1 && intResult <= 15) {
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 90)).replace(".", ",");
            }
            //de 16 a 30 = 180
            if (intResult >= 16 && intResult <= 30) {
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 180)).replace(".", ",");
            }
            //de 30 a 45 = 270
            if (intResult >= 30 && intResult <= 45) {
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 270)).replace(".", ",");
            }
            //de 46 a 60 = 360
            if (intResult >= 46 && intResult <= 60) {
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 360)).replace(".", ",");
            }
            //a partir de 60 = 450
            if (intResult > 61) {
                stPrivateDisponivilidad = (intResult) + ":" + stArrayHorasMes[1];
                stPrivateTotalExtras = String.valueOf(df.format(douHorasExtras + 450)).replace(".", ",");
            }
            booDisponivilidadVisible = true;

        } else {

            booDisponivilidadVisible = false;
        }
    }

    //
    private void consultasDB() {
        //consultar todos los registros del mes y año seleccionado
        qTodoDelMesYearSelect = "SELECT * FROM " + Utilidades.TABLA_FICHAMENSUAL
                + " WHERE " + Utilidades.CAMPO_NUM_YEAR + " = " + stYearDelQueViene
                + " AND " + Utilidades.CAMPO_NUM_MES + " = " + stMesDelQueViene
                + " ORDER BY " + Utilidades.CAMPO_NUM_DIA + " ASC ";
        //consultar el numero de regoistros del mes y año elegido
        qCuentaRegistrosDelMesYearSelect = "SELECT COUNT(*) FROM " + Utilidades.TABLA_FICHAMENSUAL
                + " WHERE " + Utilidades.CAMPO_NUM_YEAR + " = " + stYearDelQueViene
                + " AND " + Utilidades.CAMPO_NUM_MES + " = " + stMesDelQueViene;

        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn = new ConexionSQliteHelper(getActivity(), "db_fichaMensual", null, versionDb);
        //Crear la conexion con la base de datos
        SQLiteDatabase db = conn.getReadableDatabase();
        //varible para contar el numero de registros para el bucleque contará el total de horas y dietas
        int intNumeroTotalDeRegistros;
        //contar el numero de registros
        try {
            String stNumeroDeRegistros = DatabaseUtils.stringForQuery(db, qCuentaRegistrosDelMesYearSelect, null);
            intNumeroTotalDeRegistros = Integer.parseInt(stNumeroDeRegistros);
        } catch (NumberFormatException e) {
            intNumeroTotalDeRegistros = 0;
        }

        try {
            //Variable para la consulta
            //TODAS LAS HORAS del mes del año seleccionado
            String qTotalHorasMes = "SELECT " + Utilidades.CAMPO_TOTAL_HORAS + " FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + " WHERE "
                    + Utilidades.CAMPO_NUM_YEAR + " = (" + stYearDelQueViene
                    + ") AND "
                    + Utilidades.CAMPO_NUM_MES + " = (" + stMesDelQueViene + ")";
            //ADR del mes del año seleccionado
            String queryADR = qCuentaRegistrosDelMesYearSelect + " AND " + Utilidades.CAMPO_ADR + " = \"si\"";
            //DIETAS Y EXTRAS del mes del año seleccionado
            String qDietas = "SELECT " + Utilidades.CAMPO_DESAYUNO + ","
                    + Utilidades.CAMPO_COMIDA + ","
                    + Utilidades.CAMPO_CENA + ","
                    + Utilidades.CAMPO_DORMIR + ","
                    + Utilidades.CAMPO_TOTAL_DIETAS + ","
                    + Utilidades.CAMPO_EXTRAS + " FROM " + Utilidades.TABLA_FICHAMENSUAL
                    + " WHERE "
                    + Utilidades.CAMPO_NUM_YEAR + " = (" + stYearDelQueViene
                    + ") AND "
                    + Utilidades.CAMPO_NUM_MES + " = (" + stMesDelQueViene + ")";

            //pasar la consulta y recoger el dato.
            stNumeroDeDiasADR = DatabaseUtils.stringForQuery(db, queryADR, null);
            Cursor cursorTotalHorasMes = db.rawQuery(qTotalHorasMes, null);
            Cursor cursorDietas = db.rawQuery(qDietas, null);


            //comprobar que elcursor no está vacio // total horas del mes
            if (cursorTotalHorasMes != null) {
                //Mover el cursor al primer registro
                cursorTotalHorasMes.moveToFirst();
                //bucle para recorrer todos los registros
                for (int i = 0; i < intNumeroTotalDeRegistros; i++) {
                    //pasar a un string la hora de la db
                    String stHora1 = cursorTotalHorasMes.getString(0);
                    //pasar la hora al metodo de sumar horas
                    sumarHorasDelMes(stHora1);
                    cursorTotalHorasMes.moveToNext();
                }
                cursorTotalHorasMes.close();
            }

            //Sumar dietas y extras
            if (cursorDietas != null) {
                //Mover el cursor a la primera posición
                cursorDietas.moveToFirst();
                //bucle para recorrer todos los registros
                for (int i = 0; i < intNumeroTotalDeRegistros; i++) {
                    //pasar a un string el desayuno de la db
                    String stDesayuno = cursorDietas.getString(0);
                    if (stDesayuno == null) {
                        stDesayuno = "0";
                    }
                    String stComida = cursorDietas.getString(1);
                    if (stComida == null) {
                        stComida = "0";
                    }
                    String stCena = cursorDietas.getString(2);
                    if (stCena == null) {
                        stCena = "0";
                    }
                    String stDormir = cursorDietas.getString(3);
                    if (stDormir == null) {
                        stDormir = "0";
                    }
                    String stTotalDietas = cursorDietas.getString(4);
                    if (stTotalDietas == null) {
                        stTotalDietas = "0";
                    }
                    String stExtras = cursorDietas.getString(5);
                    if (stExtras == null) {
                        stExtras = "0";
                    }
                    //pasar precio al metodo de sumar dietas
                    sumarDieta(stDesayuno, stComida, stCena, stDormir, stTotalDietas, stExtras);
                    cursorDietas.moveToNext();
                }
                cursorDietas.close();
            }

        } catch (Exception ignored) {
        }

        //cerrar la base de datos
        db.close();
    }

    //Metodo para sumar las horas del mes
    private void sumarHorasDelMes(String stHora1) {

        //otra hora. hay que pasarla a minutos
        String stHora2 = stAcumuladorDeHoras;
        //un array para separar las horas de los minutos
        String[] arraySepararHora1 = stHora1.split(":");
        String[] arraySepararHora2 = stHora2.split(":");
        //pasar las horas a minutos y sumarlos
        double douSumaHoasMinutos1 = Double.parseDouble(arraySepararHora1[0]) * 60 + Double.parseDouble(arraySepararHora1[1]);
        double douSumaHoasMinutos2 = Double.parseDouble(arraySepararHora2[0]) * 60 + Double.parseDouble(arraySepararHora2[1]);
        //sumar todos los minutos y pasarlos a horas con un resto decimal de minutos
        double douMinutosSumadosPasadosEnHoras = (douSumaHoasMinutos1 + douSumaHoasMinutos2) / 60;
        //estraer las horas enteras
        int intExtraerHorasEnteras = (int) douMinutosSumadosPasadosEnHoras;
        //dejar los decimales de los minutos sin las horas
        double douSoloDecimalesMinutos = douMinutosSumadosPasadosEnHoras - intExtraerHorasEnteras;
        //pasar los decimales a horas
        double douPasarMinutosEnHoras = douSoloDecimalesMinutos * 60;
        //redondear las horas
        double douRedondeoHoras = Math.round(douPasarMinutosEnHoras);
        //eliminar decimales
        int intMinutosSinDecimales = (int) douRedondeoHoras;
        //pasar las horas a string
        String stHora = (intExtraerHorasEnteras < 10) ? "0" + (intExtraerHorasEnteras) : String.valueOf(intExtraerHorasEnteras);
        //pasar los minutos a string
        String stMinutos = (intMinutosSinDecimales < 10) ? "0" + (intMinutosSinDecimales) : String.valueOf(intMinutosSinDecimales);
        //pasar la hora completa a una cadena
        //Pasarlo al acomulador
        stAcumuladorDeHoras = stHora + ":" + stMinutos;
    }

    //metodo para sumar todas las dietas y los extras
    private void sumarDieta(String stDesayuno, String stComida, String stCena, String stDormir, String stTotalDietas, String stExtras) {
        if(stExtras.equals("")){
            stExtras = "0";
        }
        //pasar los datos a int
        double douDesayuno = Double.parseDouble(stDesayuno.replace(",", "."));
        double douComida = Double.parseDouble(stComida.replace(",", "."));
        double douCena = Double.parseDouble(stCena.replace(",", "."));
        double douDormir = Double.parseDouble(stDormir.replace(",", "."));
        double douTotalDietas = Double.parseDouble(stTotalDietas.replace(",", "."));
        double douExtras = Double.parseDouble(stExtras.replace(",", "."));
        //pasar lo acomulado a int
        double douAcumuladorDesayuno = Double.parseDouble(stPrivateDesayuno.replace(",", "."));
        double douAcumuladorComida = Double.parseDouble(stPrivateComida.replace(",", "."));
        double douAcumuladorCena = Double.parseDouble(stPrivateCena.replace(",", "."));
        double douAcumuladorDormir = Double.parseDouble(stPrivateDormir.replace(",", "."));
        double douAcumuladorTotalDietas = Double.parseDouble(stPrivateTotalDietas.replace(",", "."));
        double douAcumuladorExtras = Double.parseDouble(stPrivateExtras.replace(",", "."));
        //sumar las dietas y pasarlas al acumulador
        stPrivateDesayuno = String.valueOf(df.format(douDesayuno + douAcumuladorDesayuno)).replace(".", ",");
        stPrivateComida = String.valueOf(df.format(douComida + douAcumuladorComida)).replace(".", ",");
        stPrivateCena = String.valueOf(df.format(douCena + douAcumuladorCena)).replace(".", ",");
        stPrivateDormir = String.valueOf(df.format(douDormir + douAcumuladorDormir)).replace(".", ",");
        stPrivateTotalDietas = String.valueOf(df.format(douTotalDietas + douAcumuladorTotalDietas)).replace(".", ",");
        stPrivateExtras = String.valueOf(df.format(douExtras + douAcumuladorExtras)).replace(".", ",");
    }

    //Metodo para volver a la selección del mes.
    private void volverAlMes() {
        //Bundle el mensajero
        Bundle pasaYear = new Bundle();
        //el paquete con el contenido
        pasaYear.putString("stYearSelect", stYearDelQueViene);
        //la direccion de envio
        Fragment nuevoFragmento = new ArchivoConsultas();
        //se le pone al paquete la direccion
        nuevoFragmento.setArguments(pasaYear);
        //en caso de que el paquete esté vacio
        assert getFragmentManager() != null;
        //se escriben las instrucciones de donde y como tienen que ir
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //se ejecutan las instrucciones
        transaction.replace(R.id.content_main, nuevoFragmento);//ArchivoConsulta
        //esto no tiene servicio, es el undo
        transaction.addToBackStack(null);
        //se envia el paquete
        transaction.commit();
    }

    //Metodo  para borra la selección
    private void borrarSeleccion(){
        //
        preguntarBorrarDiaSelect();
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
        ConexionSQliteHelper conn = new ConexionSQliteHelper(getActivity(), "db_fichaMensual", null, versionDb);
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
                volverAlMes();
            }

            //reiniciar los acumuladores
            stAcumuladorDeHoras = "00:00";
            //
            stNumeroDeDiasADR = "0";
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

    //para refrescar la actividad
    public void setUserVisibleHint(boolean isVisibleToUser) {
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
        outState.putString("Mes_Seleccionado",stMesDelQueViene);
        outState.putString("Year_Seleccionado",stYearDelQueViene);
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

