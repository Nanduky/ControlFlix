package com.nanduky.controlflix;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//datePickers
import android.app.DatePickerDialog;
import android.widget.DatePicker;
//calendar
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
//utiles
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
//timrPickers
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nanduky.controlflix.utilidades.Utilidades;
import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Inicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public Inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */


    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
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

    /** Crear conexión con la base de datos **/

    //el nombre de la base de datos será db_fichaMensual
    ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);

    /* ****************************   NUEVOS ACCEOS   ******************************************** */
    //crear instancia de View que se llame vista
    View vista,vista2,vista3;
    TextView tvFecha, tvHoraIni, tvHoraFin, tvTotalResult,
            tvDesayunoTipo, tvComidaTipo, tvCenaTipo, tvNocheTipo,
            tvDesayunoPrecio,tvComidaPrecio, tvCenaPrecio, tvNochePrecio,
            tvDietaTotalResult, tvDPlaza;
    TextView tvIniVacas;

    Button btHoy, btOtroDia, btHoraIni, btHoraFin, btOotras, btGuardar, btBorrar;

    RadioButton rbSi, rbNo;
    RadioGroup rbgADR;

    //todos los cuadros de texto de las dietas
    EditText ediNde, ediNco, ediNce, ediNdo,
            ediADde, ediADco, ediADce, ediADdo,
            ediTIde, ediTIco, ediTIce, ediTIdo,
            ediITAde, ediITAco, ediITAce, ediITAdo,
            ediALEde, ediALEco, ediALEce, ediALEdo,
            ediPLco, ediPLcoADR ;

    //Añadir el ScrollView para vovlver arriba una vez guardado o borrado los datos
    ScrollView svScroll;

    //crear instancia de calendario
    public final Calendar miCalendario2 = Calendar.getInstance();


    //pasar a una varirable el dia, mes y año
    final int varYear2 = miCalendario2.get(Calendar.YEAR);
    final int varMes2 = miCalendario2.get(Calendar.MONTH);
    final int varDia2 = miCalendario2.get(Calendar.DAY_OF_MONTH);
    //Variables privadas para guardar la fecha separada par las consultas de la db
    private String stPrivadaNumDia = "";
    private String stPrivadaNumMes = "";
    private String stPrivadaNumYear = "";

    //pasar variables de la hora y minutos (inicial y final)
    final int varHoraIni2 = miCalendario2.get(Calendar.HOUR_OF_DAY);
    final int varHoraFin2 = miCalendario2.get(Calendar.HOUR_OF_DAY);
    final int varMinutosIni2 = miCalendario2.get(Calendar.MINUTE);
    final int varMinutosFin2 = miCalendario2.get(Calendar.MINUTE);

    //separador para la fecha
    private static final String barra = "/";
    //para los dias y meses menores de 2 digitos
    private static final String cero = "0";
    //separador de horas
    private static final String dosPuntos = ":";



    //variables para guardar las horas y los minutos seleccionados
    public String horaFormatIni2;
    public String horaFormatFin2;
    public String minutoFormatIni2;
    public String minutoFormatFin2;


    //variables para sumar dietas
    private double sumaDesayuno = 0;
    private double sumaComida = 0;
    private double sumaCena = 0;
    private double sumaNoche = 0;
    private double sumaDietas = 0;

    //variables para los precios de las dietas
    double douEdiNde   = 0;
    double douEdiNco   = 0;
    double douEdiNce   = 0;
    double douEdiNdo   = 0;

    double douEdiADde  = 0;
    double douEdiADco  = 0;
    double douEdiADce  = 0;
    double douEdiADdo  = 0;

    double douEdiTIde  = 0;
    double douEdiTIco  = 0;
    double douEdiTIce  = 0;
    double douEdiTIdo  = 0;

    double douEdiITAde = 0;
    double douEdiITAco = 0;
    double douEdiITAce = 0;
    double douEdiITAdo = 0;

    double douEdiALEde = 0;
    double douEdiALEco = 0;
    double douEdiALEce = 0;
    double douEdiALEdo = 0;

    double douEdiPLco  = 0;
    double douEdiPLcoADR  = 0;
    //variables para capturar el tipo de dieta de la zona y la hora
    public String dccn = ""; //desayuno comida cena dormir
    public String natiap = ""; //natiap nacional adr tir intalia alemania plaza

    //Variable para guardar los datos de los extras del sábado y/o domingo
    private String stExtrasSabyDom;
    //Variable para ver si existe una fecha o no
    private Boolean booFechaExiste = false;
    //el formato para guardar
    DecimalFormat df = new DecimalFormat("#0.00");



    /* ****************************   NUEVOS ACCEOS fin  ******************************************** */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista  = inflater.inflate(R.layout.fragment_inicio, container, false);
        vista2 = inflater.inflate(R.layout.ajustes_precio_dieta,container,false);
        vista3 = inflater.inflate(R.layout.fragment_otras_actividades2,container,false);
        /* ***************************  ON CREATE  ********************************************* */

        //crear referencias
        tvFecha =  vista.findViewById(R.id.tvFecha);
        tvHoraIni = vista.findViewById(R.id.tvHoraIni);
        tvHoraFin = vista.findViewById(R.id.tvHoraFin);
        tvTotalResult = vista.findViewById(R.id.tvTotalResult);
        tvDesayunoTipo = vista.findViewById(R.id.tvDesayunoTipo);
        tvDesayunoPrecio = vista.findViewById(R.id.tvDesayunoPrecio);
        tvComidaTipo = vista.findViewById(R.id.tvComidaTipo);
        tvComidaPrecio = vista.findViewById(R.id.tvComidaPrecio);
        tvCenaTipo = vista.findViewById(R.id.tvCenaTipo);
        tvCenaPrecio = vista.findViewById(R.id.tvCenaPrecio);
        tvNocheTipo = vista.findViewById(R.id.tvNocheTipo);
        tvNochePrecio = vista.findViewById(R.id.tvNochePrecio);
        tvDietaTotalResult = vista.findViewById(R.id.tvDietaTotalResult);

        rbSi = vista.findViewById(R.id.rbSi);
        rbNo = vista.findViewById(R.id.rbNo);
        rbgADR = vista.findViewById(R.id.rbgADR);

        btHoy = vista.findViewById(R.id.btHoy);
        btOtroDia = vista.findViewById(R.id.btOtroDia);
        btHoraIni = vista.findViewById(R.id.btHoraIni);
        btHoraFin = vista.findViewById(R.id.btHoraFin);
        btOotras = vista.findViewById(R.id.btOotras); //boton otras actividades de la pantalla de inicio
        btGuardar = vista.findViewById(R.id.btGuardar);
        btBorrar = vista.findViewById(R.id.btBorrar);



        //inflar las variables con sus editext
        ediNde = vista2.findViewById(R.id.ediNde);
        ediNco = vista2.findViewById(R.id.ediNco);
        ediNce = vista2.findViewById(R.id.ediNce);
        ediNdo = vista2.findViewById(R.id.ediNdo);

        ediADde = vista2.findViewById(R.id.ediADde);
        ediADco = vista2.findViewById(R.id.ediADco);
        ediADce = vista2.findViewById(R.id.editADce);
        ediADdo = vista2.findViewById(R.id.ediADdo);

        ediTIde = vista2.findViewById(R.id.ediTIde);
        ediTIco = vista2.findViewById(R.id.ediTIco);
        ediTIce = vista2.findViewById(R.id.ediTIce);
        ediTIdo = vista2.findViewById(R.id.ediTIdo);

        ediITAde = vista2.findViewById(R.id.ediITAde);
        ediITAco = vista2.findViewById(R.id.ediITAco);
        ediITAce = vista2.findViewById(R.id.ediITAce);
        ediITAdo = vista2.findViewById(R.id.ediITAdo);

        ediALEde = vista2.findViewById(R.id.editALEde);
        ediALEco = vista2.findViewById(R.id.editALEco);
        ediALEce = vista2.findViewById(R.id.editALEce);
        ediALEdo = vista2.findViewById(R.id.editALEdo);

        ediPLco = vista2.findViewById(R.id.editPLco);
        ediPLcoADR = vista2.findViewById(R.id.editPLcoADR);


       ////////////////////////////////////////////////////////////// tvIniVacas = vista3.findViewById(R.id.tvIniVacas);

        //El ScrollView para volver arriba
        svScroll = vista.findViewById(R.id.svScroll);


        //boton oculto hasta que me ponga a trabajar con el
        //btOotras.setVisibility(View.GONE);


        //boton para id de otras actividades a MainActivity
        btOotras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //ir al la actividad MainActivity.java----// OtrasActividades.java
                Intent  intent  = new Intent(getActivity(), MainActivity.class);
                //para enviar datos de una actividad a otra, se crea un paquete
                Bundle miPaquete = new Bundle();
                //se le dá un valor al bundel, seria meter algo en el paquete (boton otras actividades de inicio)
                miPaquete.putInt("botonOtrasActividades",R.id.btOotras);
                //se mete el bundel en el inten. seria darle el paquete al mensajero
                intent.putExtras(miPaquete);
                //se llama a la actividad
                startActivity(intent);
                //otraActividad();
            }
        });

        //el onclick de el editText de la fecha de otro dia que no sea el actual
        btOtroDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                String con = context.toString();
                //variable con el nombre del botón
                 String varBtOtrasActi = "btOtroDia";
                obtenerFecha(varBtOtrasActi, context);
            }
        });

        //el onclick para que seleccione automaticamente el dia actual
        btHoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFechaHoy();
            }
        });

        //metodo para saber cual de los dos botones de horas se ha pulsado
        btHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHoraIni2();
            }
        });

        //metodo para saber cual de los dos botones de horas se ha pulsado
        btHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectHoraFin2();
            }
        });

        //metodo para seleccionar la dieta desayuno
        tvDesayunoTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para diferenciar que dieta se ha pulsado
                dccn = "desayuno";
                selectTipoDieta(dccn);
            }
        });
        //metodo para seleccionar la dieta comida
        tvComidaTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para diferenciar que dieta se ha pulsado
                dccn = "comida";
                selectTipoDieta(dccn);
            }
        });
        //metodo para seleccionar la dieta cena
        tvCenaTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para diferenciar que dieta se ha pulsado
                dccn = "cena";
                selectTipoDieta(dccn);
            }
        });
        //metodo para seleccionar la dieta noche
        tvNocheTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //para diferenciar que dieta se ha pulsado
                dccn = "noche";
                selectTipoDieta(dccn);
            }
        });

        //metodo para control de radio boton
        rbgADR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(tvComidaTipo.getText().toString().equals("PLAZA")){
                    dccn = "comida";
                    natiap = "plaza";
                }
                addySumarDietas(dccn,natiap);
            }
        });

        //Botón de guardar los datos
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metodo para guardar los datos del formulario en la hoja mensual
                GuardarForm();
            }
        });
        //Botón de borrar el formulario
        btBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Metodo para borrar los datos del formulario.
                borrarForm();
            }
        });



        /* ***************************  ON CREATE fin ********************************************* */
        return vista;

    }


    /* *******************   METODOS PARA LA FECA Y LA HORA DE TRABAJO ************************** */

    //Metodo para guardar las prefernecias
    private void GuardarDatosShare() {
        //se crea el objeto y el nombre del archivo que guardará los datos.
        //al ser un Fragment, hay queponer el getActivity
        SharedPreferences datosShareDiaTrabajo = Objects.requireNonNull(getActivity()).getSharedPreferences("ShareDiaTrabajo",Context.MODE_PRIVATE);
        //Recoger la fecha del dia trabajado en una variable
        String shreFecha = tvFecha.getText().toString();
        //Variable boleana para el estdo del radioBoton
        boolean shareADRsi;
        boolean shareADRno;
        //Comprobar si está marcado el si o el no
        if(rbSi.isChecked()){shareADRsi = true;}else {shareADRsi = false;}
        if(rbNo.isChecked()){shareADRno = true;}else {shareADRno = false;}
        //Las horas de inicio, final y total de horas trabajadas
        String shareHoraIni = tvHoraIni.getText().toString();
        String shareHoraFin = tvHoraFin.getText().toString();
        String shareTotalResult = tvTotalResult.getText().toString();
        //Las dietas, el tipo y el precio
        String shareDesayunoTipo = tvDesayunoTipo.getText().toString();
        String shareDesayunoPrecio = tvDesayunoPrecio.getText().toString();
        String shareComidaTipo = tvComidaTipo.getText().toString();
        String shareComidaPrecio = tvComidaPrecio.getText().toString();
        String shareCenaTipo = tvCenaTipo.getText().toString();
        String shareCenaPrecio = tvCenaPrecio.getText().toString();
        String shareNocheTipo = tvNocheTipo.getText().toString();
        String shareNochePrecio = tvNochePrecio.getText().toString();
        //El total de las dietas
        String shareDietaTotalResult = tvDietaTotalResult.getText().toString();

        //Crear el objeto Editor
        SharedPreferences.Editor editorDiasTrabajo = datosShareDiaTrabajo.edit();
        //pasar el boleano de que existe
        editorDiasTrabajo.putBoolean("existe",true);
        //se le pasa al editor el dato con el nombre de la clave y el valor de la clave
        editorDiasTrabajo.putString("fecha", shreFecha);
        editorDiasTrabajo.putBoolean("ADRsi", shareADRsi);
        editorDiasTrabajo.putBoolean("ADRno", shareADRno);
        editorDiasTrabajo.putString("horaIni", shareHoraIni);
        editorDiasTrabajo.putString("horaFin", shareHoraFin);
        editorDiasTrabajo.putString("totalResult", shareTotalResult);//horar de trabajo
        editorDiasTrabajo.putString("desayunoTipo", shareDesayunoTipo);
        editorDiasTrabajo.putString("desayunoPrecio" ,shareDesayunoPrecio);
        editorDiasTrabajo.putString("comidaTipo", shareComidaTipo);
        editorDiasTrabajo.putString("comidaPrecio", shareComidaPrecio);
        editorDiasTrabajo.putString("cenaTipo", shareCenaTipo);
        editorDiasTrabajo.putString("cenaPrecio", shareCenaPrecio);
        editorDiasTrabajo.putString("nocheTipo", shareNocheTipo);
        editorDiasTrabajo.putString("nochePrecio", shareNochePrecio);
        editorDiasTrabajo.putString("dietaTotalResult", shareDietaTotalResult);
        editorDiasTrabajo.putString("numDia", stPrivadaNumDia);
        editorDiasTrabajo.putString("numMes", stPrivadaNumMes);
        editorDiasTrabajo.putString("numYear", stPrivadaNumYear);
        //almacenar los datos en el archivo
        editorDiasTrabajo.commit();

    }

    //Metodo para cargar el SharePreferences guardado
    private void CargarSharePreferences() {
        //crear o en este caso identificar el archivo de SharePreference
        SharedPreferences datosShareDiaTrabajo = getActivity().getSharedPreferences("ShareDiaTrabajo",Context.MODE_PRIVATE);
        //comprobar si el sharePrerences existe
        boolean shareExiste = datosShareDiaTrabajo.getBoolean("existe", false);
        if (shareExiste){
            //Se extrae la info del archivo o se le pone un texto por si no hay info
            String stFecha = datosShareDiaTrabajo.getString("fecha", "Fecha no seleccionada");
            Boolean bolADRsi = datosShareDiaTrabajo.getBoolean("ADRsi", false);
            Boolean bolADRno = datosShareDiaTrabajo.getBoolean("ADRno", false);
            String stHoraIni = datosShareDiaTrabajo.getString( "horaIni","Error hora" );
            String stHoraFin = datosShareDiaTrabajo.getString( "horaFin","Error hora" );
            String stTotalResult = datosShareDiaTrabajo.getString( "totalResult", "Error resultado" );
            String stDesayunoTipo = datosShareDiaTrabajo.getString( "desayunoTipo", "Error dieta" );
            String stdesayunoPrecio = datosShareDiaTrabajo.getString( "desayunoPrecio", "Error dieta" );
            String stcomidaTipo = datosShareDiaTrabajo.getString( "comidaTipo", "Error dieta" );
            String stcomidaPrecio = datosShareDiaTrabajo.getString( "comidaPrecio", "Error dieta" );
            String stcenaTipo = datosShareDiaTrabajo.getString( "cenaTipo", "Error dieta" );
            String stcenaPrecio = datosShareDiaTrabajo.getString( "cenaPrecio", "Error dieta" );
            String stnocheTipo = datosShareDiaTrabajo.getString( "nocheTipo", "Error dieta" );
            String stnochePrecio = datosShareDiaTrabajo.getString( "nochePrecio", "Error dieta" );
            String stDietaTotalResult = datosShareDiaTrabajo.getString( "dietaTotalResult", "Error resultado" );
            stPrivadaNumDia = datosShareDiaTrabajo.getString("numDia", "Error numero de dia");
            stPrivadaNumMes = datosShareDiaTrabajo.getString("numMes", "Error numero de mes");
            stPrivadaNumYear = datosShareDiaTrabajo.getString("numYear", "Error numero de año");
            //Se pone los datos en sus etiquetas
            tvFecha.setText(stFecha);
            tvHoraIni.setText(stHoraIni);
            tvHoraFin.setText(stHoraFin);
            rbSi.setChecked(bolADRsi);
            rbNo.setChecked(bolADRno);
            tvTotalResult.setText(stTotalResult);
            tvDesayunoTipo.setText(stDesayunoTipo);
            tvDesayunoPrecio.setText(stdesayunoPrecio);
            tvComidaTipo.setText(stcomidaTipo);
            tvComidaPrecio.setText(stcomidaPrecio);
            tvCenaTipo.setText(stcenaTipo);
            tvCenaPrecio.setText(stcenaPrecio);
            tvNocheTipo.setText(stnocheTipo);
            tvNochePrecio.setText(stnochePrecio);
            tvDietaTotalResult.setText(stDietaTotalResult);

            //Extraer el estdo del radioBoton de adr
            Boolean booADRsi = datosShareDiaTrabajo.getBoolean("ADRsi", false);
            //Comprobar cual está pulsado y volver a marcarlo
            if(booADRsi == true){
                //si se el si se marca y se desmarca el no
                rbSi.setChecked(true);
                rbNo.setChecked(false);
            }
            else {
                //Si es el no, se hace lo contrari
                rbSi.setChecked(false);
                rbNo.setChecked(true);
            }
        }

    }

/* ******************************por aqui********************************* */
    //el metodo para obtener la fecha actual
    private void obtenerFechaHoy() {
        //aumentar el mes en uno, ya que se inicia en cero
        final int mesActual = varMes2 +1;
        //para poner un cero si es menor de 10
        String diaFormat = (varDia2 < 10)? cero + String.valueOf(varDia2) : String.valueOf(varDia2);
        //Para poner un cero denlante del mes si es menor de 10
        String mesFormat = (mesActual < 10)? cero + String.valueOf(mesActual) : String.valueOf(mesActual);
        String result  = (diaFormat + barra + mesFormat + barra + varYear2);
        tvFecha.setText(result);
        //guaradar la fecha separada para el SQLite
        stPrivadaNumDia = diaFormat;
        stPrivadaNumMes = mesFormat;
        stPrivadaNumYear = String.valueOf(varYear2);
        //comprobar que año hay para la seleccionar el precio de la dieta
        dietaSeleccionada();
    }//el metodo para obtener la fecha actual fin

    //el metodo para obtene la fecha
    public void obtenerFecha(final String varBtOtrasActi, Context conTXT) {
        Context contexto = getActivity();
        //bucle para comprobar que botón se pulsó
        switch (varBtOtrasActi) {
            case "btOtroDia":
                contexto = getActivity();
                break;
            case "btIniVacas": //esto era para aprobechar el codigo para la fecha de las vacaciones,
                               // pero no funcionó
                contexto = conTXT;
                break;
                default:
                    break;
        }
        //Crear un datePicker
        final DatePickerDialog miDataPickerDialog2 = new DatePickerDialog(contexto, new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int dtpYear2, int dtpMes2, int dtpDia2) {
                //aumentar el mes en uno, ya que se inicia en cero
                final int mesActual2 = dtpMes2 +1;
                //para poner un cero delante si es menor de 10
                String diaFormat2 = (dtpDia2 < 10)? cero + String.valueOf(dtpDia2) : String.valueOf(dtpDia2);
                //para poner un cero dalante si es menor de 10
                String mesFormat2 = (mesActual2 < 10)? cero + String.valueOf(mesActual2 ): String.valueOf(mesActual2);

                //el resultado del dia en una variable.
                String conca = diaFormat2 + barra + mesFormat2 + barra + dtpYear2;

                //guaradar la fecha separada para el SQLite
                stPrivadaNumDia = diaFormat2;
                stPrivadaNumMes = mesFormat2;
                stPrivadaNumYear = String.valueOf(dtpYear2);

                //bucle para poner en la etiqueta adecuada el texto corresondiente
                switch (varBtOtrasActi) {
                    case "btOtroDia":
                        //la fecha formateada meterla en el cuadro de texto
                        tvFecha.setText( conca);
                        break;
                     /** no consigo mandar la fecha a otrasActividades.java sin que me dé un error
                    ya que este datapicker hace dos vueltas al codigo y luego cierra la ventana
                    de dialogo, y aqui en la primera vuelta se le dice que se vaya a la otra
                    actividad y al no cerrar el cuadro de dialogo me dá un error **/

                    default:
                        break;
                }
                //comprobar que año hay para la seleccionar el precio de la dieta
                dietaSeleccionada();
            }

        },varYear2 , varMes2, varDia2);
        miDataPickerDialog2.show();
    }//el metodo para obtene la fecha fin

    //para seleccionar la hora de inicio
    private void selectHoraIni2() {

        //crear timerPicker
        TimePickerDialog miTimerPickerDialogIni2 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int tpdHoraIni2, int tpdMinutoIni2) {

                //para poner un cero delante en caso de que sea menor de 10 para las horas y los minutos
                String horaFormat2 = (tpdHoraIni2 < 10)? cero + String.valueOf(tpdHoraIni2) : String.valueOf(tpdHoraIni2);
                String minutosFormat2 = (tpdMinutoIni2 < 10)? cero + String.valueOf(tpdMinutoIni2) : String.valueOf(tpdMinutoIni2);
                //string con la cadena formateada
                String ResultadoFormat = horaFormat2 + dosPuntos + minutosFormat2;


                //el resultado en la casilla correspondiente de hora inicial
                tvHoraIni.setText(ResultadoFormat);

                //guardar la hora seleccionada
                horaFormatIni2 = horaFormat2;
                minutoFormatIni2 = minutosFormat2;


                //comprobar se la horaFin está guardada en el sharePreferences
                SharedPreferences comprobarDatosShare  = getActivity().getSharedPreferences("ShareDiaTrabajo", Context.MODE_PRIVATE);
                String comprobarHoraFin = comprobarDatosShare.getString("horaFin", null);


                //comprobar que no está vacio la otra casilla de la hora
                if(!tvHoraFin.getText().toString().isEmpty() && horaFormatFin2 != null) {
                    CalcularHoras2();
                }
                //en el caso de que no esté vacia porque se ha rellenado con el sharePreferences
                else if(comprobarHoraFin != null && !comprobarHoraFin.equals("")){
                    //se le pasa a las variables de la horaFin las horas y los minutos
                    horaFormatFin2 = comprobarHoraFin.substring(0,2);
                    minutoFormatFin2 = comprobarHoraFin.substring(3,5);
                    CalcularHoras2();
                }
            }
        }, varHoraIni2, varMinutosIni2, true);

        //lanzar el reloj para seleccionar hora
        miTimerPickerDialogIni2.show();
    }    //para seleccionar la hora de inicio fin

    //para seleccionar la hora de final
    private void selectHoraFin2() {

        //crear timerPicker
        TimePickerDialog miTimerPickerDialogIni2 = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int tpdHoraFin2, int tpdMinutoFin2) {

                //para poner un cero delante en caso de que sea menor de 10 para las horas y los minutos
                String horaFormat2 = (tpdHoraFin2 < 10)? cero + String.valueOf(tpdHoraFin2) : String.valueOf(tpdHoraFin2);
                String minutosFormat2 = (tpdMinutoFin2 < 10)? cero + String.valueOf(tpdMinutoFin2) : String.valueOf(tpdMinutoFin2);
                //string con la cadena formateada
                String ResultadoFormat = horaFormat2 + dosPuntos + minutosFormat2;

                //el resultado en la casilla correspondiente de hora inicial
                tvHoraFin.setText(ResultadoFormat);

                //guardar la hora seleccionada
                horaFormatFin2 = horaFormat2;
                minutoFormatFin2 = minutosFormat2;

                //comprobar se la horaFin está guardada en el sharePreferences
                SharedPreferences comprobarDatosShare  = getActivity().getSharedPreferences("ShareDiaTrabajo", Context.MODE_PRIVATE);
                String comprobarHoraIni = comprobarDatosShare.getString("horaIni", null);



                //comprobar que no está vacio la otra casilla de la hora
                if(!tvHoraIni.getText().toString().isEmpty() && horaFormatIni2 != null) {
                    CalcularHoras2();
                }
                //en el caso de que no esté vacia porque se ha rellenado con el sharePreferences
                else if(comprobarHoraIni != null && !comprobarHoraIni.equals("")){
                    //se le pasa a las variables de la horaFin las horas y los minutos
                    horaFormatIni2 = comprobarHoraIni.substring(0,2);
                    minutoFormatIni2 = comprobarHoraIni.substring(3,5);
                    CalcularHoras2();
                }
            }
        }, varHoraFin2, varMinutosFin2, true);

        //lanzar el reloj para seleccionar hora
        miTimerPickerDialogIni2.show();
    }//para seleccionar la hora de final fin

    //metodo para calcular la diferencia de horas de inicio y final de la jornada
    private void CalcularHoras2() {


        //se cgen los datos de las variables de las horas y minutos
        //y se pasan a unas variables String
        String strhIni2 = String.valueOf(horaFormatIni2);
        String strmIni2 = String.valueOf(minutoFormatIni2);
        String strhFin2 = String.valueOf(horaFormatFin2);
        String strmFin2 = String.valueOf(minutoFormatFin2);

        //pasar a numero
        //se convierten las cadenas en numeros
        int intHoraIni2 = Integer.parseInt(strhIni2);
        int intMinutosIni2 = Integer.parseInt(strmIni2);
        int intHoraFin2 = Integer.parseInt(strhFin2);
        int intMinutosFin2 = Integer.parseInt(strmFin2);
        //para meter el resultado
        double douVarResultMinutos2;
        //el resultado de pasar las horas a minutos mas los minutos (iniciales)
        double douResultMinutosIni2;
        //el resultado de pasar las horas a minutos mas los minutos (finales)
        double douResultMinutosFin2;
        //pasar las horas enteras sin los miutos restantes
        int varIntResultHoras2;

        //pasar las horas a minutos
        //todos los minutos iniciales
        douResultMinutosIni2 = (intHoraIni2 * 60) + intMinutosIni2;
        //todos los minutos finales
        douResultMinutosFin2 = (intHoraFin2 * 60) + intMinutosFin2;

        //comprobar si la hora de inicio es mayor o menor de la del finla
        if(douResultMinutosIni2 > douResultMinutosFin2){
            douResultMinutosFin2 += 1440;
        }

        //sacar el total de munutos trabajados
        douVarResultMinutos2 = douResultMinutosFin2 - douResultMinutosIni2;

        //pasar a horas los minutos
        double douVarResultMinutosPasaHoras2 = douVarResultMinutos2 / 60;

        //sacar las horas enteras
        varIntResultHoras2 = (int)douVarResultMinutosPasaHoras2;

        //saca lo mintos restantes en fracciones de hora
        double  douVarResultSoloMinutos2 = (douVarResultMinutosPasaHoras2 - varIntResultHoras2)*60;

        douVarResultSoloMinutos2 = Math.round(douVarResultSoloMinutos2);
        //eliminar los decimales
        int intVarResultMinutos2 = (int)douVarResultSoloMinutos2;


        //si los minutos son inferiores de 10
        String varResultMinutos3 = (intVarResultMinutos2 < 10)? cero + String.valueOf(intVarResultMinutos2): String.valueOf(intVarResultMinutos2);




        tvTotalResult.setText(varIntResultHoras2 + dosPuntos + varResultMinutos3);

    }//metodo para calcular la diferencia de horas de inicio y final de la jornada fin

    /* *******************   METODOS PARA LA FECA Y LA HORA DE TRABAJO FIN *********************  */
    /* ****************************************************************************************** */
    /* ****************************************************************************************** */
    /* ****************************************************************************************** */
    /* ****************************   METODOS PARA LAS DIETAS   ********************************* */

    //Metodo para seleccionar el tipo de dieta
    private void selectTipoDieta(final String dccn) {

        //crear el dialogo para seleccionar la dieta
        final AlertDialog.Builder builderDieta = new AlertDialog.Builder(getActivity());
        //añadir el xml
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v2 = inflater.inflate(R.layout.dialog_fragment_tipo_dietas, null);
        tvDPlaza = v2.findViewById(R.id.tvDPlaza);
        //si se pulsa en un textView que no sea el de comida, que sea invisible la dieta plaza
        if(!dccn.equals("comida")) {
            tvDPlaza.setVisibility(View.GONE);
        }
        builderDieta.setView(v2);

        //emparentar etiquetas del xml
        TextView tvDNac = v2.findViewById(R.id.tvDNac);
        TextView tvDADR = v2.findViewById(R.id.tvDADR);
        TextView tvDTIR = v2.findViewById(R.id.tvDTIR);
        TextView tvDItalia = v2.findViewById(R.id.tvDItalia);
        TextView tvDAlemania = v2.findViewById(R.id.tvDAlemania);
        TextView tvDPlaza = v2.findViewById(R.id.tvDPlaza);
        TextView tvDBorrar = v2.findViewById(R.id.tvDBorrar);





        //DIETA NACIONAL
        tvDNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "nacional";
                addySumarDietas(dccn, natiap);
            }
        });
        //DIETA ADR
        tvDADR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "adr";
                addySumarDietas(dccn, natiap);
            }
        });
        //DIETA TIR
        tvDTIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "tir";
                addySumarDietas(dccn, natiap);
            }
        });
        //DIETA ITALIANA
        tvDItalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "italia";
                addySumarDietas(dccn, natiap);
            }
        });
        //DIETA ALEMANA
        tvDAlemania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "alemania";
                addySumarDietas(dccn, natiap);
            }
        });
        //DIETA PLAZA
        tvDPlaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "plaza";
                addySumarDietas(dccn, natiap);
            }
        });
        //BORRAR DIETA
        tvDBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                natiap = "borrar";
                addySumarDietas(dccn, natiap);
            }
        });

        //Mostrar el cuadro de dialogo
        alerta = builderDieta.show();
    }

    //metodo para añadir en cada casilla el tipo de dieta y el precio y sumarlo toodo
    private void addySumarDietas(String dccn, String natiap) {



        //pasar los datos


        douEdiNde   = Double.parseDouble(ediNde   .getText().toString().replace(",","."));
        douEdiNco   = Double.parseDouble(ediNco   .getText().toString().replace(",","."));
        douEdiNce   = Double.parseDouble(ediNce   .getText().toString().replace(",","."));
        douEdiNdo   = Double.parseDouble(ediNdo   .getText().toString().replace(",","."));

        douEdiADde  = Double.parseDouble(ediADde   .getText().toString().replace(",","."));
        douEdiADco  = Double.parseDouble(ediADco  .getText().toString().replace(",","."));
        douEdiADce  = Double.parseDouble(ediADce  .getText().toString().replace(",","."));
        douEdiADdo  = Double.parseDouble(ediADdo  .getText().toString().replace(",","."));

        douEdiTIde  = Double.parseDouble(ediTIde  .getText().toString().replace(",","."));
        douEdiTIco  = Double.parseDouble(ediTIco  .getText().toString().replace(",","."));
        douEdiTIce  = Double.parseDouble(ediTIce  .getText().toString().replace(",","."));
        douEdiTIdo  = Double.parseDouble(ediTIdo  .getText().toString().replace(",","."));

        douEdiITAde = Double.parseDouble(ediITAde  .getText().toString().replace(",","."));
        douEdiITAco = Double.parseDouble(ediITAco .getText().toString().replace(",","."));
        douEdiITAce = Double.parseDouble(ediITAce .getText().toString().replace(",","."));
        douEdiITAdo = Double.parseDouble(ediITAdo .getText().toString().replace(",","."));

        douEdiALEde = Double.parseDouble(ediALEde .getText().toString().replace(",","."));
        douEdiALEco = Double.parseDouble(ediALEco .getText().toString().replace(",","."));
        douEdiALEce = Double.parseDouble(ediALEce .getText().toString().replace(",","."));
        douEdiALEdo = Double.parseDouble(ediALEdo .getText().toString().replace(",","."));

        douEdiPLco  = Double.parseDouble(ediPLco  .getText().toString().replace(",","."));
        douEdiPLcoADR  = Double.parseDouble(ediPLcoADR  .getText().toString().replace(",","."));


        //ir al metodo que mete la dieta seleccionada
        dietaSeleccionada();

    }

    //metodo que que mete la dieta seleccionada
    private void dietaSeleccionada() {
        //aqui comprobar el año para el precio de la dieta
        //pasar el año de string a interger
        int intPrivateYear = Integer.parseInt(stPrivadaNumYear);
        //ir al metodo que comprueba el año
        compruebaYear(intPrivateYear);
        //Variable con el tipo de dieta
        String nac = "NACIONAL";
        String adr = "ADR";
        String tir = "TIR";
        String ita = "ITALIA";
        String ale = "ALEMANIA";
        String pla = "PLAZA";
        //capturar el estado de los radiobotones
        boolean estadoRBsi = rbSi.isChecked();
        boolean estadoRBno = rbNo.isChecked();

        switch (dccn){
            case "desayuno":
                switch (natiap){
                    case "nacional":
                        tvDesayunoTipo.setText(nac);
                        tvDesayunoPrecio.setText(String.valueOf(douEdiNde));
                        sumaDesayuno = douEdiNde;
                        alerta.dismiss();
                        break;
                    case "adr":
                        tvDesayunoTipo.setText(adr);
                        tvDesayunoPrecio.setText(String.valueOf(douEdiADde));
                        sumaDesayuno = douEdiADde;
                        alerta.dismiss();
                        break;
                    case "tir":
                        tvDesayunoTipo.setText(tir);
                        tvDesayunoPrecio.setText(String.valueOf(douEdiTIde));
                        sumaDesayuno = douEdiTIde;
                        alerta.dismiss();
                        break;
                    case "italia":
                        tvDesayunoTipo.setText(ita);
                        tvDesayunoPrecio.setText(String.valueOf(douEdiITAde));
                        sumaDesayuno = douEdiITAde;
                        alerta.dismiss();
                        break;
                    case "alemania":
                        tvDesayunoTipo.setText(ale);
                        tvDesayunoPrecio.setText(String.valueOf(douEdiALEde));
                        sumaDesayuno = douEdiALEde;
                        alerta.dismiss();
                        break;
                    case "borrar":
                        tvDesayunoTipo.setText("");
                        tvDesayunoPrecio.setText("");
                        sumaDesayuno = 0;
                        alerta.dismiss();
                        break;
                }
                break;
            case "comida":
                switch (natiap){
                    case "nacional":
                        tvComidaTipo.setText(nac);
                        tvComidaPrecio.setText(String.valueOf(douEdiNco));
                        sumaComida = douEdiNco;
                        alerta.dismiss();
                        break;
                    case "adr":
                        tvComidaTipo.setText(adr);
                        tvComidaPrecio.setText(String.valueOf(douEdiADco));
                        sumaComida = douEdiADco;
                        alerta.dismiss();
                        break;
                    case "tir":
                        tvComidaTipo.setText(tir);
                        tvComidaPrecio.setText(String.valueOf(douEdiTIco));
                        sumaComida = douEdiTIco;
                        alerta.dismiss();
                        break;
                    case "italia":
                        tvComidaTipo.setText(ita);
                        tvComidaPrecio.setText(String.valueOf(douEdiITAco));
                        sumaComida = douEdiITAco;
                        alerta.dismiss();
                        break;
                    case "alemania":
                        tvComidaTipo.setText(ale);
                        tvComidaPrecio.setText(String.valueOf(douEdiALEco));
                        sumaComida = douEdiALEco;
                        alerta.dismiss();
                        break;
                    case "plaza":
                        tvComidaTipo.setText(pla);
                        if(estadoRBno){
                            tvComidaPrecio.setText(String.valueOf(douEdiPLco));
                            sumaComida = douEdiPLco;
                        }
                        else if(estadoRBsi) {
                            tvComidaPrecio.setText(String.valueOf(douEdiPLcoADR));
                            sumaComida = douEdiPLcoADR;
                        }
                        //comprobar si viene dedes el radioBoton
                        if(alerta == null){
                            break;
                        }
                        alerta.dismiss();
                        break;
                    case "borrar":
                        tvComidaTipo.setText("");
                        tvComidaPrecio.setText("");
                        sumaComida = 0;
                        alerta.dismiss();
                        break;
                }
                break;
            case "cena":
                switch (natiap){
                    case "nacional":
                        tvCenaTipo.setText(nac);
                        tvCenaPrecio.setText(String.valueOf(douEdiNce));
                        sumaCena = douEdiNce;
                        alerta.dismiss();
                        break;
                    case "adr":
                        tvCenaTipo.setText(adr);
                        tvCenaPrecio.setText(String.valueOf(douEdiADce));
                        sumaCena = douEdiADce;
                        alerta.dismiss();
                        break;
                    case "tir":
                        tvCenaTipo.setText(tir);
                        tvCenaPrecio.setText(String.valueOf(douEdiTIce));
                        sumaCena = douEdiTIce;
                        alerta.dismiss();
                        break;
                    case "italia":
                        tvCenaTipo.setText(ita);
                        tvCenaPrecio.setText(String.valueOf(douEdiITAce));
                        sumaCena = douEdiITAce;
                        alerta.dismiss();
                        break;
                    case "alemania":
                        tvCenaTipo.setText(ale);
                        tvCenaPrecio.setText(String.valueOf(douEdiALEce));
                        sumaCena = douEdiALEce;
                        alerta.dismiss();
                        break;
                    case "borrar":
                        tvCenaTipo.setText("");
                        tvCenaPrecio.setText("");
                        sumaCena = 0;
                        alerta.dismiss();
                        break;
                }
                break;
            case "noche":
                switch (natiap){
                    case "nacional":
                        tvNocheTipo.setText(nac);
                        tvNochePrecio.setText(String.valueOf(douEdiNdo));
                        sumaNoche = douEdiNdo;
                        alerta.dismiss();
                        break;
                    case "adr":
                        tvNocheTipo.setText(adr);
                        tvNochePrecio.setText(String.valueOf(douEdiADdo));
                        sumaNoche = douEdiADdo;
                        alerta.dismiss();
                        break;
                    case "tir":
                        tvNocheTipo.setText(tir);
                        tvNochePrecio.setText(String.valueOf(douEdiTIdo));
                        sumaNoche = douEdiTIdo;
                        alerta.dismiss();
                        break;
                    case "italia":
                        tvNocheTipo.setText(ita);
                        tvNochePrecio.setText(String.valueOf(douEdiITAdo));
                        sumaNoche = douEdiITAdo;
                        alerta.dismiss();
                        break;
                    case "alemania":
                        tvNocheTipo.setText(ale);
                        tvNochePrecio.setText(String.valueOf(douEdiALEdo));
                        sumaNoche = douEdiALEdo;
                        alerta.dismiss();
                        break;
                    case "borrar":
                        tvNocheTipo.setText("");
                        tvNochePrecio.setText("");
                        sumaNoche = 0;
                        alerta.dismiss();
                        break;
                }
                break;
        }
        Locale spanish = new Locale("es", "ES");
        /*El SharePreferent no pasa aqui los datos, los pone en la casilla de la dieta. Hay que comprobar
        si la casilla tiene un dato y pasarlo a la suma*/
        //Declarar las variantes double para pasar los datos de las casillas
        double douDesay, douComi, douCen, douNoch;
        //comprobar que el desayuno no esté vacio
        if (!tvDesayunoPrecio.getText().toString().equals("") && !tvDesayunoPrecio.getText().toString().equals("0,00")) {
            douDesay = Double.parseDouble(tvDesayunoPrecio.getText().toString());
            sumaDesayuno = douDesay;
        }
        //comprobar que la comida no esté vacia
        if (!tvComidaPrecio.getText().toString().equals("") && !tvComidaPrecio.getText().toString().equals("0,00")) {
            douComi = Double.parseDouble(tvComidaPrecio.getText().toString());
            sumaComida = douComi;
        }
        //comprobar que la cena no esté vacia
        if (!tvCenaPrecio.getText().toString().equals("") && !tvCenaPrecio.getText().toString().equals("0,00")) {
            douCen = Double.parseDouble(tvCenaPrecio.getText().toString());
            sumaCena = douCen;
        }
        //comprobar que la noche no esté vacia
        if (!tvNochePrecio.getText().toString().equals("") && !tvNochePrecio.getText().toString().equals("0,00")) {
            douNoch = Double.parseDouble(tvNochePrecio.getText().toString());
            sumaNoche = douNoch;
        }

        sumaDietas = sumaDesayuno + sumaComida + sumaCena + sumaNoche;
        tvDietaTotalResult.setText(String.format(spanish,"%.2f",sumaDietas));
        if(tvDietaTotalResult.getText().toString().equals("0,00")){
            tvDietaTotalResult.setText("");
        }
    }

    //metodo que comprueba el año para la dieta
    private void compruebaYear(int intPrivateYear) {
        // si es un año inferior a 2020 se pone la dieta antigua.
        if (intPrivateYear < 2020) {
            douEdiNde   = 5.35; douEdiNco   = 14.16; douEdiNce   = 14.16; douEdiNdo   = 13.33;
            douEdiADde  = 5.48; douEdiADco  = 14.50; douEdiADce  = 14.50; douEdiADdo  = 13.65;
            douEdiTIde  = 8.16; douEdiTIco  = 23.05; douEdiTIce  = 21.28; douEdiTIdo  = 19.86;
            douEdiITAde = 8.72; douEdiITAco = 24.64; douEdiITAce = 22.75; douEdiITAdo = 21.23;
            douEdiALEde = 10.21; douEdiALEco = 28.84; douEdiALEce = 26.62; douEdiALEdo = 24.85;
            douEdiPLco  = 10.35; douEdiPLcoADR  = 10.60;

        }
        // si es el año 2020 se pone la fecha de su año
        if (intPrivateYear == 2020) {
            douEdiNde   = 5.51; douEdiNco   = 14.59; douEdiNce   = 14.59; douEdiNdo   = 13.73;
            douEdiADde  = 5.64; douEdiADco  = 14.94;  douEdiADce  = 14.94; douEdiADdo  = 14.06;
            douEdiTIde  = 8.40; douEdiTIco  = 23.75; douEdiTIce  = 21.92; douEdiTIdo  = 20.46;
            douEdiITAde = 8.98; douEdiITAco = 25.39; douEdiITAce = 23.44; douEdiITAdo = 21.88;
            douEdiALEde = 10.51; douEdiALEco = 29.72; douEdiALEce = 27.43; douEdiALEdo = 25.60;
            douEdiPLco  = 10.67; douEdiPLcoADR  = 10.92;

        }
        // si es el año 2021 se pone la fecha de su año
        if (intPrivateYear == 2021) {
            douEdiNde   = 5.59; douEdiNco   = 14.81; douEdiNce   = 14.81; douEdiNdo   = 13.94;
            douEdiADde  = 5.72; douEdiADco  = 15.16;  douEdiADce  = 15.16; douEdiADdo  = 14.27;
            douEdiTIde  = 8.53; douEdiTIco  = 24.11; douEdiTIce  = 22.25; douEdiTIdo  = 20.77;
            douEdiITAde = 9.12; douEdiITAco = 25.78; douEdiITAce = 23.79; douEdiITAdo = 22.21;
            douEdiALEde = 10.67; douEdiALEco = 30.16; douEdiALEce = 27.84; douEdiALEdo = 25.99;
            douEdiPLco  = 10.83; douEdiPLcoADR  = 11.08;

        }
        // si es el año 2022 se pone la fecha de su año
        if (intPrivateYear == 2022) {
            douEdiNde   = 5.67; douEdiNco   = 15.03; douEdiNce   = 15.03; douEdiNdo   = 14.15;
            douEdiADde  = 5.80; douEdiADco  = 15.39;  douEdiADce  = 15.39; douEdiADdo  = 14.48;
            douEdiTIde  = 8.66; douEdiTIco  = 24.47; douEdiTIce  = 22.58; douEdiTIdo  = 21.08;
            douEdiITAde = 9.26; douEdiITAco = 26.16; douEdiITAce = 24.14; douEdiITAdo = 22.54;
            douEdiALEde = 10.84; douEdiALEco = 30.62; douEdiALEce = 28.25; douEdiALEdo = 26.38;
            douEdiPLco  = 10.99; douEdiPLcoADR  = 11.25;

        }
        // si es el año 2023 se pone la fecha de su año
        if (intPrivateYear == 2023) {
            douEdiNde   = 5.76; douEdiNco   = 15.26; douEdiNce   = 15.26; douEdiNdo   = 14.36;
            douEdiADde  = 5.90; douEdiADco  = 15.62;  douEdiADce  = 15.62; douEdiADdo  = 14.70;
            douEdiTIde  = 8.79; douEdiTIco  = 24.84; douEdiTIce  = 22.92; douEdiTIdo  = 21.40;
            douEdiITAde = 9.40; douEdiITAco = 26.55; douEdiITAce = 24.50; douEdiITAdo = 22.88;
            douEdiALEde = 11.00; douEdiALEco = 31.08; douEdiALEce = 28.67; douEdiALEdo = 26.77;
            douEdiPLco  = 11.15; douEdiPLcoADR  = 11.41;

        }
        // TODO Para añadir las dietas de más años
    }

    /* ****************************   METODOS PARA LAS DIETAS   ********************************* */


    //un dialogo en private para poder cerrarlo una vez elegida la dieta
    private Dialog alerta;

    @Override
    public void onResume() {
        super.onResume();
        //Metodo para cargar el SharePrefecrences
        CargarSharePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Al pulsar el botón para ir a otrasActividades (pasando por MainActivity) hay que
        //guardar primero los campos ya rellenados.
        //Se crea un SharePrefernce para guardar los campos
        GuardarDatosShare();
    }

    /* ****************************   METODOS PARA LAS DIETAS FIN   ***************************** */


    /* **************** METODOS PARA LOS BOTONES PARA GUARDAR Y BORRAR DATOS   ****************** */

    //metodo para guardar el formulario.
    private void GuardarForm() {

        //comprobar que esté todos los capmos obligatorios rellenos
        //pasar los datos de los TextView avariables
        String stFechaCampos = tvFecha.getText().toString();
        String stHoraIniCampos = tvHoraIni.getText().toString();
        String stHoraFinCampos = tvHoraFin.getText().toString();
        //variables para darles un valor según la elección
        int intFechaCampos = 0;
        int intHoraIniCampos = 0;
        int intHoraFinCampos =0;
        int intOtrasCampos =0;
        //Asignar los valores correspondientes
        if(!stFechaCampos.equals("")){intFechaCampos = 100;}
        if(!stHoraIniCampos.equals("")){intHoraIniCampos = 10;}
        if(!stHoraFinCampos.equals("")){intHoraFinCampos = 1;}
        //Sumar todos los valores
        int intResultCampos = intFechaCampos + intHoraIniCampos + intHoraFinCampos + intOtrasCampos;
        //Bucle para saber que se seleccionó
        switch (intResultCampos){
            case 0:         //nada seleccionado
            case 1:         //hora final
            case 10:        //hora inicial
            case 11:        //hora inicial y hora final
            case 1001:      //otras acticvidades y hora final
            case 1010:      //otras actividades y hora inicial
            case 1011:      //otras actividades, hora inicial y hora final
                Toast.makeText(getContext(),"Tienes que seleccionar una fecha", Toast.LENGTH_SHORT).show();
                break;
            case 100:       //fecha
            case 101:       //fecha y hora final
            case 1100:      //otras actividades y fecha
            case 1101:      //otras actividades,fecha y hora final
                Toast.makeText(getActivity(),"Tienes que seleccionar una hora de inicio", Toast.LENGTH_SHORT).show();
                break;
            case 110:       //fecha y hora inical
            case 1110:      //otras actividades, fecha y hora inicial
                Toast.makeText(getActivity(),"Tienes que seleccionar una hora final", Toast.LENGTH_SHORT).show();
                break;
            case 111:       //fecha, hora inicial y hora final
            case 1000:      //otras actividades
            case 1111:      //todoo

                //comprobar los otros campos si están vacios y asignarles un valor para que no queden
                //vacios en la db
                String ceroCero = "0,00";
                if(tvDesayunoPrecio.getText().toString().equals("")){
                    tvDesayunoPrecio.setText(ceroCero);
                }
                if(tvComidaPrecio.getText().toString().equals("")){
                    tvComidaPrecio.setText(ceroCero);
                }
                if(tvCenaPrecio.getText().toString().equals("")){
                    tvCenaPrecio.setText(ceroCero);
                }
                if(tvNochePrecio.getText().toString().equals("")){
                    tvNochePrecio.setText(ceroCero);
                }
                if(tvDietaTotalResult.getText().toString().equals("")){
                    tvDietaTotalResult.setText(ceroCero);
                }
                //variable para pasar lo que se ha seleccionado
                String stSelectFechas = "";
                if(intResultCampos == 111){
                    stSelectFechas = "fechaUnDia";
                }
                else if(intResultCampos == 1000){
                    stSelectFechas = "fechasOtrasActividades";
                }
                else if(intResultCampos == 1111){
                    stSelectFechas = "todasLasFechas";
                }

                // comprobar la fecha si existe. la fecha estoa en stFechaCampos
                comprobarFechaExiste(stFechaCampos);
                //comprobar si exsiste la fecha a guardar
                if(booFechaExiste == false){
                    //metodo para guardar los datos en el base de datos (bd_fichaMensual)
                    registrarDatosDia(stSelectFechas);
                    //borrar todos los datos del formulario
                    borrarFormGuardado();
                }
                else{
                    Toast.makeText(getActivity(),"Esta fecha ya estaba guardada", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;

        }
    }
    //Variable publica para pnerle nombre al dia elegido
    public String nombreDelDia;

    //Metodo para guardar los datos en la base de datos
    private void registrarDatosDia(String stSelectFechas) {

        /** Crear conexión con la base de datos **/
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Abrir la tabla en modo escritura
        SQLiteDatabase bd = conn.getWritableDatabase();
        //Para ver el numero de registro para poner el campo id se hace una conslulta a la bd
        //Variable que contendrá el numero de registros
        int intNumeroDeRegistros;
        //Si aún no hubiese regisros lanzaria un error, creamos un try / catch
        //si hay registros, usamos el try
        try{
            //se crea una variable con la consulta
            String query = "SELECT MAX(id) FROM fichaMensual";
            //Se pasa la consulta y se guarda el resultado en una variable
            String stNumerosDeRegistros = DatabaseUtils.stringForQuery(bd, query,null);
            //se pasa la cadena a numero y se le suma 1 (para un registro más)
            intNumeroDeRegistros = Integer.parseInt(stNumerosDeRegistros) + 1;

        }//En el caso de que no hayan registros, se captura el error
        catch (NumberFormatException e) {
            //Se le pasa a la variable el numero 0 (de 0 registros)
            intNumeroDeRegistros = 0;
        }
        //crear el contentValues
        ContentValues values = new ContentValues();
        //Variable para saber el estado del radio botón del ADR
        String stEstadoADR;
        //Se le pasa al value todos los datos
        values.put(Utilidades.CAMPO_ID, intNumeroDeRegistros);

        //Si se seleccionó solo la fecha de un dia
        if(stSelectFechas.equals("fechaUnDia")){

            //Metodo para obtene el nombre del dia.
            obtenerNombreDia(tvFecha.getText().toString());
            //se le pasa a la variable el estado del botón.
            if(rbSi.isChecked()){ stEstadoADR = "si";} else {stEstadoADR = "no";}
            //guardar la fecha completa
            values.put(Utilidades.CAMPO_FECHA,tvFecha.getText().toString() );
            values.put(Utilidades.CAMPO_OTRAS_ACTIVIDADES,"");//el nombre de las otras actividades
            values.put(Utilidades.CAMPO_OTRAS_ACTIVIDADES_RETRIBUIDO,"");//retribuido si o no
            values.put(Utilidades.CAMPO_DIA, nombreDelDia);//el nombre del dia
            values.put(Utilidades.CAMPO_ADR, stEstadoADR);//el radio botón del ADR
            values.put(Utilidades.CAMPO_HORA_INI, tvHoraIni.getText().toString());//la hora de inicio
            values.put(Utilidades.CAMPO_HORA_FIN, tvHoraFin.getText().toString());//la hora final
            values.put(Utilidades.CAMPO_TOTAL_HORAS, tvTotalResult.getText().toString());//el total de horas trabajadas
            values.put(Utilidades.CAMPO_DESAYUNO, tvDesayunoPrecio.getText().toString());//el desayuno
            values.put(Utilidades.CAMPO_COMIDA, tvComidaPrecio.getText().toString());//la comida
            values.put(Utilidades.CAMPO_CENA, tvCenaPrecio.getText().toString());//la cena
            values.put(Utilidades.CAMPO_DORMIR, tvNochePrecio.getText().toString());//la noche
            values.put(Utilidades.CAMPO_TOTAL_DIETAS, tvDietaTotalResult.getText().toString());//el total de las dietas
            //para obtener los extras de los sabados y domingos
            obtenerExtras(tvTotalResult, tvFecha);
            values.put(Utilidades.CAMPO_EXTRAS, stExtrasSabyDom);
            //para las consultas de la db
              values.put(Utilidades.CAMPO_NUM_DIA, stPrivadaNumDia);
            values.put(Utilidades.CAMPO_NUM_MES, stPrivadaNumMes);
            values.put(Utilidades.CAMPO_NUM_YEAR, stPrivadaNumYear);

        }



        Long idResultante = bd.insert(Utilidades.TABLA_FICHAMENSUAL,Utilidades.CAMPO_ID,values);
        bd.close();

        Toast.makeText(getActivity(), nombreDelDia + " " + tvFecha.getText().toString() + " Guardado Correctamente. ", Toast.LENGTH_LONG).show();
    }

    private void comprobarFechaExiste(String stOtrasActividadesFechaVacasIni) {
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Conectar con la db
        SQLiteDatabase db = conn.getReadableDatabase();
        //se crea una variable con una consulta
        String query  = "SELECT COUNT(" + Utilidades.CAMPO_FECHA + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
                        + " WHERE " + Utilidades.CAMPO_FECHA + " = '" + stOtrasActividadesFechaVacasIni + "'";
        //se pasa a una variable el numero de registrso
        String stNumeroDeRegistrosDeFechas = DatabaseUtils.stringForQuery(db,query,null);
        int intNumeroDeRegistrosDeFechas = Integer.parseInt(stNumeroDeRegistrosDeFechas);

        if(intNumeroDeRegistrosDeFechas == 0){
            //si no hay repetidos
            booFechaExiste = false;
        }
        else {
            //si hay repetidos
            booFechaExiste = true;
        }
        db.close();
    }

    private void obtenerExtras(TextView tvTotalResult, TextView tvFecha) {
        //Sacar los datos de los TextView en variables
        //el total de horas
        String stTotalResult = tvTotalResult.getText().toString();
        //Array para separar las horas y los minutos.
        String[] arrayHorasYMinutos = stTotalResult.split(":");
        //pasarlo a una variable y convertirlos a int
        int intHora = Integer.parseInt(arrayHorasYMinutos[0]);
        int intMminutos = Integer.parseInt(arrayHorasYMinutos[1]);
        //pasarlo todos a minutos.
        int intTotalMinutos =(intHora * 60) + intMminutos;
        //Variable con el valos de 4 horas de trabajo y la de 8 horas
        final int intCuatroHoras = 240;
        final int intOchoHoras = 480;
        //la fecha
        String stFecha = tvFecha.getText().toString();
        //extraer el nombre del dia de la fecha
        obtenerNombreDia(stFecha);
        //Si es sabado
        if(nombreDelDia.equals("sábado") || nombreDelDia.equals("domingo")){
            //se mira que cantidad de extra le toca por las horas
            if(intTotalMinutos >= 00 && intTotalMinutos <= 59){
                //de 0 a 1 hora, nada
                stExtrasSabyDom = "00,00";
            }
            if(intTotalMinutos >= 60 && intTotalMinutos <= intCuatroHoras){
                //de 1 a 4 horas, 50 euros
                stExtrasSabyDom = "50,00";
            }
            if(intTotalMinutos >= (intCuatroHoras + 1) && intTotalMinutos <= intOchoHoras){
                //de 4 a 8 horas, 100 euros
                stExtrasSabyDom = "100,00";
            }
            if(intTotalMinutos > intOchoHoras){
                // más de 8 horas, 100 euros más 13,15 por hora extra
                //el precio de la hora extra
                double douPrecioHoraExtra = 13.15;
                stExtrasSabyDom = String.valueOf(df.format(((intHora - 8) * douPrecioHoraExtra) + 100));

            }
        }
        else{
            //Si no es sabado o domingo, se pone vacio los extras
            stExtrasSabyDom="";
        }
    }

    //Metodo para obtene el nombre del dia seleccionado.
    @SuppressLint("SimpleDateFormat")
    private void obtenerNombreDia(String stNombreDia) {
        //Formatear la fecha elegida
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        //crear una fecha vacia
        Date date=null;
        //para que no de error si hay un fallo
        try {
            //pasarle a la fecha vacia la fecha seleccionada y formatearla
            date = formatter.parse(stNombreDia);
        } catch (ParseException e) {
            //en caso de error
            Toast.makeText(getActivity(), "No hay una fecha seleccionada", Toast.LENGTH_LONG).show();
            return;
        }
        formatter = new SimpleDateFormat("EEEE");
        nombreDelDia = formatter.format(date);


    }

    //metodo para borrar el formulario.
    private void borrarForm() {
        //Crear un aviso antes de borrar por si se le dió por error
        AlertDialog.Builder alertaBorrado = new AlertDialog.Builder(getActivity());
        //Se le pone el titulo
        alertaBorrado.setTitle("¡¡¡ ATENCIÓN !!!");
        //Se le pone un mensaje
        alertaBorrado.setMessage("Estás apunto de borrar el formulario. \n¿Estás seguro?");
        //Se le pone los botones
        alertaBorrado.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //se crea el objeto y el nombre del archivo del que se quiere borrar los datos.
                //al ser un Fragment, hay queponer el getActivity
                SharedPreferences datosShareDiaTrabajo = getActivity().getSharedPreferences("ShareDiaTrabajo",Context.MODE_PRIVATE);
                //se crea una variable con el valor vacio
                String shreFecha = "";
                //Crear el objeto Editor
                SharedPreferences.Editor editorDiasTrabajo = datosShareDiaTrabajo.edit();
                //se le pasa al editor el dato con el nombre de la clave y el valor de la clave vacio
                editorDiasTrabajo.putString("fecha",shreFecha);
                //almacenar los datos en el archivo
                editorDiasTrabajo.commit();
                //se elimina el valod del TextView
                //La fecha del dia seleccionado
                tvFecha.setText("");
                //Radio boton SI o NO
                rbSi.setChecked(false);
                rbNo.setChecked(true);
                //La casilla de otras Actividades
                //Botones de inicio y final de la jornada
                String selecHora = "Selecciona hora";
                tvHoraIni.setText(""); tvHoraIni.setHint(selecHora);
                tvHoraFin.setText(""); tvHoraFin.setHint(selecHora);
                //Total de horas trabajadas
                tvTotalResult.setText("");
                //Todas las dietas
                String selec = "Selecciona";
                String ceroCero = "0,00";
                //Desayuno
                tvDesayunoTipo.setText(""); tvDesayunoTipo.setHint(selec);
                tvDesayunoPrecio.setText(""); tvDesayunoPrecio.setHint(ceroCero);
                //comida
                tvComidaTipo.setText(""); tvComidaTipo.setHint(selec);
                tvComidaPrecio.setText(""); tvComidaPrecio.setHint(ceroCero);
                //cena
                tvCenaTipo.setText(""); tvCenaTipo.setHint(selec);
                tvCenaPrecio.setText(""); tvCenaPrecio.setHint(ceroCero);
                //dormir
                tvNocheTipo.setText(""); tvNocheTipo.setHint(selec);
                tvNochePrecio.setText(""); tvNochePrecio.setHint(ceroCero);
                //total de las dietas
                tvDietaTotalResult.setText(""); tvDietaTotalResult.setHint(ceroCero);
                //las variables de las dietas
                sumaDesayuno = 0;
                sumaComida = 0;
                sumaCena = 0;
                sumaNoche = 0;
                sumaDietas = 0;
                //borrar el tipo de dieta y el precio
                dccn = "";
                natiap = "";
                //borrar los datos para la consilta de la db
                stPrivadaNumDia = "";
                stPrivadaNumMes = "";
                stPrivadaNumYear= "";
                //Mensaje que se ha borrado el formulatio.
                Toast.makeText(getContext(),"Se ha borrardo el formulario",Toast.LENGTH_LONG).show();
                //Despúes de borrar el from se vuelve arriba, esto se hace con el Scroll
                svScroll.scrollTo(0, 0);

                //borrar los datos del SharePreferent
                borraDatosShareGuardados();

            }
        });
        alertaBorrado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertaBorrado.show();
    }


    private void borrarFormGuardado() {
        //se crea el objeto y el nombre del archivo del que se quiere borrar los datos.
        //al ser un Fragment, hay queponer el getActivity
        SharedPreferences datosShareDiaTrabajo = getActivity().getSharedPreferences("ShareDiaTrabajo",Context.MODE_PRIVATE);
        //se crea una variable con el valor vacio
        String shreFecha = "";
        //Crear el objeto Editor
        SharedPreferences.Editor editorDiasTrabajo = datosShareDiaTrabajo.edit();
        //se le pasa al editor el dato con el nombre de la clave y el valor de la clave vacio
        editorDiasTrabajo.putString("fecha",shreFecha);
        //almacenar los datos en el archivo
        editorDiasTrabajo.commit();
        //se elimina el valod del TextView
        //La fecha del dia seleccionado
        tvFecha.setText("");
        //Radio boton SI o NO
        rbSi.setChecked(false);
        rbNo.setChecked(true);
        //Botones de inicio y final de la jornada
        String selecHora = "Selecciona hora";
        tvHoraIni.setText(""); tvHoraIni.setHint(selecHora);
        tvHoraFin.setText(""); tvHoraFin.setHint(selecHora);
        //Total de horas trabajadas
        tvTotalResult.setText("");
        //Todas las dietas
        String selec = "Selecciona";
        String ceroCero = "0,00";
        //Desayuno
        tvDesayunoTipo.setText(""); tvDesayunoTipo.setHint(selec);
        tvDesayunoPrecio.setText(""); tvDesayunoPrecio.setHint(ceroCero);
        //comida
        tvComidaTipo.setText(""); tvComidaTipo.setHint(selec);
        tvComidaPrecio.setText(""); tvComidaPrecio.setHint(ceroCero);
        //cena
        tvCenaTipo.setText(""); tvCenaTipo.setHint(selec);
        tvCenaPrecio.setText(""); tvCenaPrecio.setHint(ceroCero);
        //dormir
        tvNocheTipo.setText(""); tvNocheTipo.setHint(selec);
        tvNochePrecio.setText(""); tvNochePrecio.setHint(ceroCero);
        //total de las dietas
        tvDietaTotalResult.setText(""); tvDietaTotalResult.setHint(ceroCero);
        //las variables de las dietas
        sumaDesayuno = 0;
        sumaComida = 0;
        sumaCena = 0;
        sumaNoche = 0;
        sumaDietas = 0;
        //borrar el tipo de dieta y el precio
        dccn = "";
        natiap = "";
        //Despúes de borrar el from se vuelve arriba, esto se hace con el Scroll
        svScroll.scrollTo(0, 0);

        //borrar los datos del SharePreferent
        borraDatosShareGuardados();

    }


    //Borrar los datos del SharePrefecences que hayan guadados.
    private void borraDatosShareGuardados() {
        //se crea el objeto y el nombre del archivo que se quiere borrar los datos.
        //al ser un Fragment, hay queponer el getActivity
        SharedPreferences borrarDatosShareDiaTrabajo = getActivity().getSharedPreferences("ShareDiaTrabajo",Context.MODE_PRIVATE);
        //Crear el objeto Editor
        SharedPreferences.Editor editorDiasTrabajo = borrarDatosShareDiaTrabajo.edit();
        //se le pasa al editor el dato con el nombre de la clave y el valor de la clave
        editorDiasTrabajo.putBoolean("existe", true);
        boolean shareADRsi = false;
        boolean shareADRno = true;
        editorDiasTrabajo.putBoolean("ADRsi", shareADRsi);
        editorDiasTrabajo.putBoolean("ADRno", shareADRno);
        editorDiasTrabajo.putString("horaIni", "");
        editorDiasTrabajo.putString("horaFin", "");
        editorDiasTrabajo.putString("totalResult", "");//horar de trabajo
        editorDiasTrabajo.putString("desayunoTipo", "");
        editorDiasTrabajo.putString("desayunoPrecio" ,"");
        editorDiasTrabajo.putString("comidaTipo", "");
        editorDiasTrabajo.putString("comidaPrecio", "");
        editorDiasTrabajo.putString("cenaTipo", "");
        editorDiasTrabajo.putString("cenaPrecio", "");
        editorDiasTrabajo.putString("nocheTipo", "");
        editorDiasTrabajo.putString("nochePrecio", "");
        editorDiasTrabajo.putString("dietaTotalResult", "");
        editorDiasTrabajo.putString("numDia", "");
        editorDiasTrabajo.putString("numMes", "");
        editorDiasTrabajo.putString("numYear", "");
        //almacenar los datos en el archivo
        editorDiasTrabajo.commit();
    }

    /* **************** METODOS PARA LOS BOTONES PARA GUARDAR Y BORRAR DATOS   ****************** */






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
