package com.nanduky.controlflix;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nanduky.controlflix.utilidades.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtrasActividades2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtrasActividades2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtrasActividades2 extends Fragment {


    /*  ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼    NUEVOS ACCEOS    ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  */
    //vista
    View vista;
    //botones
    Button btIniOtrasActividades,btFinalOtrasActividades,btAceptarOtrasActividades,btCancelarOtrasActividades;
    //EditText
    EditText etNombreOtrasActividades,etHorasComputo,etMinutoComputo;
    //TextView
    TextView tvIniOtrasActividades,tvFinalOtrasActividades;

    //radiobotones
    RadioButton rbVacas, rbBaja,rbAsuntos,rbFestivo,rbSanciones,rbOtros,rbRetribuidoSi,rbRetribuidoNo;
    RadioGroup rgSelec, rgRetribuido;

    //LinearLayout
    LinearLayout lyNombreOtraActividad,lyBotonInicio,lyBotonFinal,lySumarComputo;



    //crear instancia de calendario
    public final Calendar miCalendarioOtrasActividades = Calendar.getInstance();
    //pasar a una varirable el dia, mes y año
    final int varYear = miCalendarioOtrasActividades.get(Calendar.YEAR);
    final int varMes = miCalendarioOtrasActividades.get(Calendar.MONTH);
    final int varDia = miCalendarioOtrasActividades.get(Calendar.DAY_OF_MONTH);


    //separador para la fecha
    private  static final String barra = "/";
    //para los dias y meses menores de 2 digitos
    private static final String cero = "0";
    //quitar espacios en blanco delante y detras del editText nombre
     String stNombreTrim = "";
     //variable para guardar el nombre del dia de la fecha a guardar
    String stNombreDelDia;
    //Variables privadas para guardar la fecha separada par las consultas de la db
    private String stPrivadaNumDia = "";
    private String stPrivadaNumMes = "";
    private String stPrivadaNumYear = "";
    //variable con el nombre del radio boton pulsado
    private String stRBpulsado = "";
    //si el radioboton es el el otros
    private String stRBpulsadoOtros = "";
    //variable que recoge si es retribuido o no
    private String stRetribuido = "";
    //boleano para ver si se pulsó rbOtros y el rbSi
    boolean booOtrosSi = false;


    //crear variables para pasar los datos de los textView.
    private String stComprobarFechaIni = "";
    //crear variables para comprobar la hora y los minutos a sumar al computo
    private String pstHorasComputo = "";
    private String pstMinutosComputo = "";
    private String stTotalHoras = "00:00";


    //declarar variable string para pasar el nombre del boton pulsado.
    String stBotonPulsadoOtrasActividades;


    /* ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ NUEVOS ACCEOS FIN ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ */
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OtrasActividades2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtrasActividades2.
     */
    // TODO: Rename and change types and number of parameters
    public static OtrasActividades2 newInstance(String param1, String param2) {
        OtrasActividades2 fragment = new OtrasActividades2();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*  ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  NUEVAS REFERENCIAS  ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼  */
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_otras_actividades2, container, false);



        //TextView's
        tvIniOtrasActividades = vista.findViewById(R.id.tvIniOtrasActividades);
        tvFinalOtrasActividades = vista.findViewById(R.id.tvFinalOtrasActividades);

        //botones
        btIniOtrasActividades = vista.findViewById(R.id.btIniOtrasActividades);
        btFinalOtrasActividades = vista.findViewById(R.id.btFinalOtrasActividades);
        //desactivar el botón hasta que esté todoo correcto
        btAceptarOtrasActividades = vista.findViewById(R.id.btAceptarOtrasActividades);
        btAceptarOtrasActividades.setEnabled(false);
        btCancelarOtrasActividades = vista.findViewById(R.id.btCancelarOtrasActividades);

        //EditText
        etNombreOtrasActividades = vista.findViewById(R.id.etNombreOtrasActividades);
        etHorasComputo = vista.findViewById(R.id.etHorasComputo);
        etMinutoComputo = vista.findViewById(R.id.etMinutoComputo);

        //LinearLayout
        //que no sea visible hasta que no se seleccione el radio buton otros para poner el nombre
        lySumarComputo = vista.findViewById(R.id.lySumarComputo);
        lySumarComputo.setVisibility(View.GONE);
        //que no sea visible hasta que se pulse el rbSI
        lyNombreOtraActividad = vista.findViewById(R.id.lyNombreOtraActividad);
        lyNombreOtraActividad.setVisibility(View.GONE);

        //que no sean visibles hasta que no se selecione un radio buton
        lyBotonInicio = vista.findViewById(R.id.lyBotonInicio);
        lyBotonInicio.setVisibility(View.GONE);
        lyBotonFinal = vista.findViewById(R.id.lyBotonFinal);
        lyBotonFinal.setVisibility(View.GONE);

        //radiobotones
        rbVacas = vista.findViewById(R.id.rbVacas);
        rbBaja = vista.findViewById(R.id.rbBaja);
        rbAsuntos = vista.findViewById(R.id.rbAsuntos);
        rbFestivo = vista.findViewById(R.id.rbFestivo);
        rbSanciones = vista.findViewById(R.id.rbSanciones);
        rbOtros = vista.findViewById(R.id.rbOtros);
        rbRetribuidoSi = vista.findViewById(R.id.rbRetribuidoSi);
        rbRetribuidoNo = vista.findViewById(R.id.rbRetribuidoNo);
        rgSelec = vista.findViewById(R.id.rgSelec);
        //que no sea visible hasta que no se seleccione el radio buton otros o asuntos propios
        rgRetribuido = vista.findViewById(R.id.rgRetribuido);
        rgRetribuido.setVisibility(View.GONE);

        //Comprobar si seleccionó algun rb y mostrar lo que toque mostrar
        rgSelec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                lyBotonInicio.setVisibility(View.VISIBLE);
                lyBotonFinal.setVisibility(View.VISIBLE);
                //Ir al metodo que comprueba que rb se pulsú
                radiobotonSelect();
            }
        });
        rgRetribuido.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radiobotonSelect();
            }
        });

        //botones para seleccionar la fecha
        btIniOtrasActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String para pasar en nombre del boton pulsado
                stBotonPulsadoOtrasActividades = "btIniOtrasActividades";
                //ir al metodo para seleccinar la fecha de inicio de las vacaciones.
                ObtenerFechaOtrasActividades(stBotonPulsadoOtrasActividades);
            }
        });
        btFinalOtrasActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String para pasar en nombre del boton pulsado
                stBotonPulsadoOtrasActividades = "btFinalOtrasActividades";
                //ir al metodo para seleccinar la fecha de inicio de las vacaciones.
                ObtenerFechaOtrasActividades(stBotonPulsadoOtrasActividades);
            }
        });

        //el boton de cancelar que lo borra toddo
        btCancelarOtrasActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarFormularioOtrasActividades();
            }
        });

        //para comprobar el campo del nombre
        etNombreOtrasActividades.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //comprobar el nombre introducido
                comprobarNombre();
                return false;
            }

            private void comprobarNombre() {
                //guardar el texto introducido en el edit text
                String stnombreIntroducido = etNombreOtrasActividades.getText().toString();
                stNombreTrim = " " + stnombreIntroducido.trim();
                //si el nombre no está vacio se muestra el botón acetar
                if ( stNombreTrim.equals("")) {
                    stNombreTrim = " Otra actividad";
                }
                comprobarCamposRellenos();
            }
        });

        //validar las horas introducidas segun se van introduciendo
        etHorasComputo.addTextChangedListener(new TextWatcher() {
            //variable para la hora
            int hora;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //si no está vacio
                if (!etHorasComputo.getText().toString().isEmpty()) {
                    //se le pasa la hora introducida
                    hora = Integer.parseInt(etHorasComputo.getText().toString());
                } else {
                    //si está vacio, se le pasa 0
                    hora = 0;
                }
                //no puede ser maayor de 23, sino se borra
                if (hora > 23) {
                    etHorasComputo.setText("");
                    etHorasComputo.setHint("00");
                    Toast.makeText(getContext(),"Hora no valida",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //validar los minutos segun se van introduciendo
        etMinutoComputo.addTextChangedListener(new TextWatcher() {
            //variable para los minutos
            int minutos;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //si no está vacio
                if (!etMinutoComputo.getText().toString().isEmpty()) {
                    //se le pasa los minutos introducidos.
                    minutos = Integer.parseInt(etMinutoComputo.getText().toString());
                }else{
                    minutos = 0;
                }
                //no puede ser mayor de 59
                if (minutos > 59) {
                    etMinutoComputo.setText("");
                    etMinutoComputo.setHint("00");
                    Toast.makeText(getContext(),"Minutos no validos",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //el boton aceptar
        btAceptarOtrasActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //variables con las fechas seleccionadas
                String stFechaIni = tvIniOtrasActividades.getText().toString();
                String stFechaFin = tvFinalOtrasActividades.getText().toString();
                //si se pulsó el otros y el si
                if (booOtrosSi) {
                    //se comprueba si hay algo escrito en el editText
                    stRBpulsadoOtros =etNombreOtrasActividades.getText().toString();
                    //si no hay nada escrito se le pasa un nombre
                    if(stRBpulsadoOtros.equals("")){
                        stRBpulsado = " Otra actividad";
                    }
                    //di hay algo escrito, se le pasa el nombre con un espacio delante
                    else {
                        stRBpulsado = " " +  stRBpulsadoOtros;
                    }
                }

                //crear el formato adecuado
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat  sdfFechas = new SimpleDateFormat("dd/MM/yyyy");
                //crear un calendar
                Calendar calendar = Calendar.getInstance();

                try {
                    //extraer las fechas seleccionadas
                    Date dtFechaIni = sdfFechas.parse(stFechaIni);
                    Date dtFechaFin = sdfFechas.parse(stFechaFin);
                    //eliminar el posible error
                    assert dtFechaIni != null; assert dtFechaFin != null;
                    //dias de diferencia
                    int intDiasDiferneciaVacas = (int) ((dtFechaFin.getTime() - dtFechaIni.getTime()) / 86400000)+1;
                    //recoger la fecha
                    calendar.setTime(dtFechaIni);
                    //bucle que recorre el total de dias seleccionados
                    for(int i = 0; i<intDiasDiferneciaVacas;i++){
                        calendar.add(Calendar.DAY_OF_YEAR,1);
                        //meter la fecha formateada en una variable
                        String stFechaFormat = sdfFechas.format(dtFechaIni);
                        dtFechaIni = calendar.getTime();
                        //pasar la fecha a la variable local
                        stComprobarFechaIni = stFechaFormat;
                        //comprobar si exsite la fecha
                        //comprobar los campos de las horas y los minutos del computo
                        comprobarCamposComputo();
                        comprobarFechaExiste(stFechaFormat);
                        Log.i("es: ",stFechaFormat);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //borrar el formulario
                borrarFormularioOtrasActividades();

            }
        });



        /* ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ NUEVAS REFERENCIAS FIN ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ */
        return vista;
    }




    /* ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ METODOS ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼ */

    //variable para contar el numero de dias que se repite
    private int rep = 0;


    //comprobar cual se pulsó
    private void radiobotonSelect() {

        //variable para recoger el numero que luego servirá para enseñar lo que haga falta
        int intMostrar = 0;
        //pasar las id a una variable int
        int idSelec = rgSelec.getCheckedRadioButtonId();
        int idRetri = rgRetribuido.getCheckedRadioButtonId();
        //crear un bucle que recorra la id
        switch (idSelec){
            //si se pulsó vacacionies
            case R.id.rbVacas:
                //pasar nombre al string
                stRBpulsado = " Vacaciones";
                stRetribuido = "si";
                //pasar el valor al int
                intMostrar = 1;
                booOtrosSi = false;
                //ir al metodo mostrar
                mostrar(intMostrar,stRBpulsado);
                //detener bucle
                break;
            //si se pulsó baja
            case R.id.rbBaja:
                stRBpulsado = " Baja";
                stRetribuido = "si";
                booOtrosSi = false;
                intMostrar = 1;
                mostrar(intMostrar,stRBpulsado);
                break;
            //si se pulsó festivos
            case R.id.rbFestivo:
                stRBpulsado = " Festivo";
                stRetribuido = "si";
                booOtrosSi = false;
                intMostrar = 1;
                mostrar(intMostrar,stRBpulsado);
                break;
            //si se pulsó sanciones
            case R.id.rbSanciones:
                stRBpulsado = " Sanción";
                stRetribuido = "no";
                booOtrosSi = false;
                intMostrar = 1;
                mostrar(intMostrar,stRBpulsado);
                break;
            //si se pulsó asuntos propios
            case R.id.rbAsuntos:
                stRBpulsado = " Asuntos propios";
                intMostrar = 2;
                booOtrosSi = false;
                mostrar(intMostrar,stRBpulsado);
                //bucle para comprobar si es retribuido o no
                switch (idRetri) {
                    //si se pulsó si
                    case R.id.rbRetribuidoSi:
                        //pasar valor al int
                        intMostrar = 3;
                        stRetribuido = "si";
                        booOtrosSi = false;
                        //ir al metodo mostrar
                        mostrar(intMostrar,stRBpulsado);
                        //detener bucle
                        break;
                    //si se pulsó no
                    case R.id.rbRetribuidoNo:
                        intMostrar = 4;
                        stRetribuido = "no";
                        booOtrosSi = false;
                        mostrar(intMostrar,stRBpulsado);
                        break;
                        default:break;
                }
                break;
            //si se pulsó otros
            case R.id.rbOtros:
                intMostrar = 5;
                mostrar(intMostrar,stRBpulsado);
                //bucle para comprobar si es retribuido o no
                switch (idRetri) {
                    //si se pulsó si
                    case R.id.rbRetribuidoSi:
                        //pasar valor al int
                        intMostrar = 6;
                        stRetribuido = "si";
                        stRBpulsado = "otros";
                        booOtrosSi = true;
                        //ir al metodo mostrar
                        mostrar(intMostrar,stRBpulsado);
                        //detener bucle
                        break;
                    //si se pulsó no
                    case R.id.rbRetribuidoNo:
                        intMostrar = 7;
                        stRetribuido = "no";
                        booOtrosSi = false;
                        mostrar(intMostrar,stRBpulsado);
                        break;
                    default:break;
                }
                break;
            default:
                break;
        }

    }

    //metodo que mostrará los los layauts y/o botones según se necesite
    private void mostrar(int intMostrar,String stRBpulsado) {
        //bucle que recorre el valor del int
        switch (intMostrar) {
            //El caso 0 en teoria nunca existirá
            case 0:
                btAceptarOtrasActividades.setEnabled(false);
                rgRetribuido.setVisibility(View.GONE);
                lyNombreOtraActividad.setVisibility(View.GONE);
                etNombreOtrasActividades.setText("");
                lySumarComputo.setVisibility(View.GONE);
                stNombreTrim = "";
                break;
            //El caso 1, vacas, baja, festivos y sanciones
            case 1:
                //ir al metodo que comprueba si están toddos los campos necesarios rellenos
                comprobarCamposRellenos();
                //esconder el radio grupo que no se usará
                rgRetribuido.setVisibility(View.GONE);
                //en el caso de que se pulsara algun rb de este grupo, se borran
                if (rbRetribuidoSi.isChecked() || rbRetribuidoNo.isChecked()) {
                    descheca();
                }
                //borrar el nombre del editText
                etNombreOtrasActividades.setText("");
                //en caso de que esté visible, se oculta el layaut para poner nombre a la otra actividad
                lyNombreOtraActividad.setVisibility(View.GONE);
                //vaciar la variable con el nombre
                stNombreTrim = "";
                //se detiene el bucle
                break;
            //El caso 2 es asuntos propios pero sin un si o no
            case 2:
                //primero se hace visible el grupo si ono
                rgRetribuido.setVisibility(View.VISIBLE);
                //pero se desactiva el botón aceptar.
                btAceptarOtrasActividades.setEnabled(false);
                //borrar el nombre del editText
                etNombreOtrasActividades.setText("");
                //en caso de que esté visible, se oculta el layaut para poner nombre a la otra actividad
                lyNombreOtraActividad.setVisibility(View.GONE);
                //vaciar la variable con el nombre
                stNombreTrim = "";
                //se detiene el bucle
                break;
            //el caso 3 y 4 son el si o el no de asuntos propios
            case 3:
            case 4:
                //ir al metodo que comprueba si están toddos los campos necesarios rellenos
                comprobarCamposRellenos();
                //se detiene el bucle
                break;
            //el caso 5 es el otros sin el si o el no
            case 5:
                //primero se hace visible el grupo si ono
                rgRetribuido.setVisibility(View.VISIBLE);
                //pero se desactiva el botón aceptar.
                btAceptarOtrasActividades.setEnabled(false);
                //se detiene el bucle
                break;
                // el 6  si de otros
            case 6:
                //mostrar el layaut con las horas que hay que poner
                lySumarComputo.setVisibility(View.VISIBLE);
                //ir al metodo que comprueba si están toddos los campos necesarios rellenos
                comprobarCamposRellenos();
                //se muestra el layaut para poner el nombre a la otra actividad
                lyNombreOtraActividad.setVisibility(View.VISIBLE);
                //se detiene el bucle
                break;
                //y 7 el no de otros
            case 7:
                //ocultar el layaut con las horas que hay que poner
                lySumarComputo.setVisibility(View.GONE);
                //ir al metodo que comprueba si están toddos los campos necesarios rellenos
                comprobarCamposRellenos();
                //se muestra el layaut para poner el nombre a la otra actividad
                lyNombreOtraActividad.setVisibility(View.VISIBLE);
                //se detiene el bucle
                break;
                default:
                    break;
        }
    }

    //metodo par deschecar el rg retribuido
    private void descheca() {
        rgRetribuido.clearCheck();
    }

    //Metodo para obtener la fechas de las otras actividades
    public void ObtenerFechaOtrasActividades(final String stBotonPulsadoOtrasActividades) {


        //Crear un DatePickerDialof
        final DatePickerDialog miDateOtrasActividades = new DatePickerDialog(Objects.requireNonNull(getContext()) , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int dtpYear, int dtpMes, int dtpDia) {
                //Aumentar el mes en uno, ya que se inicia en 0
                final int mesActualOtraActividad = dtpMes + 1;
                //Si el día elegido es menor de 10, se le pone un cero delante
                String diaFormat = (dtpDia < 10)? cero + String.valueOf(dtpDia) : String.valueOf(dtpDia);
                //Si el mes seleccionado es menor de diez, se le pone un coro delante.
                String mesFormat = (mesActualOtraActividad < 10? cero + String.valueOf(mesActualOtraActividad) : String.valueOf(mesActualOtraActividad));

                //La fecha completa en una variable String
                String fechaOtraActividades = diaFormat + barra + mesFormat + barra + dtpYear;
                //guaradar la fecha separada para el SQLite
                stPrivadaNumDia = diaFormat;
                stPrivadaNumMes = mesFormat;
                stPrivadaNumYear = String.valueOf(dtpYear);

                //bucle para comprobar que boton se pulsó y poner la fecha en su etiqueta.
                switch (stBotonPulsadoOtrasActividades){
                    case "btIniOtrasActividades":
                        tvIniOtrasActividades.setText(fechaOtraActividades);
                        //metodo para comprobar que esten los campos necesarios rellenos
                        comprobarCamposRellenos();
                        break;
                    case "btFinalOtrasActividades":
                        tvFinalOtrasActividades.setText(fechaOtraActividades);
                        //metodo para comprobar que esten los campos necesarios rellenos
                        comprobarCamposRellenos();
                        break;
                    default:
                        break;
                }
            }
        },varYear, varMes, varDia);
        miDateOtrasActividades.show();


    }
    //Metodo para comprobar si existe la fecha
    private void comprobarFechaExiste(String stFechaFormat) {
        //el nombre de la base de datos será db_fichaMensual
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        //Conectar con la db
        SQLiteDatabase db = conn.getReadableDatabase();
        //se crea una variable con una consulta
        String query  = "SELECT COUNT(" + Utilidades.CAMPO_FECHA + ") FROM " + Utilidades.TABLA_FICHAMENSUAL
                + " WHERE " + Utilidades.CAMPO_FECHA + " = '" + stFechaFormat + "'";
        //se pasa a una variable el numero de registrso
        String stNumeroDeRegistrosDeFechas = DatabaseUtils.stringForQuery(db,query,null);
        int intNumeroDeRegistrosDeFechas = Integer.parseInt(stNumeroDeRegistrosDeFechas);


        if(intNumeroDeRegistrosDeFechas == 0){
            //si no hay repetidos
            guardarFecha(stFechaFormat);
        }
        else {
            //si hay repetidos
            rep++;

        }
        if (rep == 1) {
            Toast.makeText(getActivity(), "No hay nada que guardar\nComprueba que las fechasno están ya guardadas.", Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    //Metodo para obtene el nombre del dia seleccionado.
    @SuppressLint("SimpleDateFormat")
    private void obtenerNombreDia(String stNombreDia) {
        //Formatear la fecha elegida
        SimpleDateFormat sdfNombreDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfDia = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfMes = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfYear = new SimpleDateFormat("dd/MM/yyyy");
        //crear una fecha vacia
        Date dateNombreDia = null;
        Date dateDia = null;
        Date dateMes = null;
        Date dateYear = null;
        //para que no de error si hay un fallo
        try {
            //pasarle a la fecha vacia la fecha seleccionada y formatearla
            dateNombreDia = sdfNombreDia.parse(stNombreDia);
            dateDia = sdfDia.parse(stNombreDia);
            dateMes = sdfMes.parse(stNombreDia);
            dateYear = sdfYear.parse(stNombreDia);
        } catch (ParseException e) {
            //en caso de error
            Toast.makeText(getActivity(), "No hay una fecha seleccionada", Toast.LENGTH_LONG).show();
            return;
        }
        sdfNombreDia = new SimpleDateFormat("EEEE");
        sdfDia = new SimpleDateFormat("dd");
        sdfMes = new SimpleDateFormat("MM");
        sdfYear = new SimpleDateFormat("yyyy");
        if(dateNombreDia != null){
            stNombreDelDia = sdfNombreDia.format(dateNombreDia);
            assert dateDia != null;
            stPrivadaNumDia = sdfDia.format(dateDia);
            assert dateMes != null;
            stPrivadaNumMes = sdfMes.format(dateMes);
            assert dateYear != null;
            stPrivadaNumYear = sdfYear.format(dateYear);
        }


    }

    //metodo para comprobar que esten los campos necesarios rellenos
    private void comprobarCamposRellenos() {

        //pasar los datos de los textView a una variable
        stComprobarFechaIni = tvIniOtrasActividades.getText().toString();
        String stComprobarFechaFinal = tvFinalOtrasActividades.getText().toString();
        //pasar los datos de los editText para el computo a una variable
        pstMinutosComputo = etMinutoComputo.getText().toString();
        pstHorasComputo = etHorasComputo.getText().toString();

        //Comprobar que no hayan campos vacios
        //si alguna de las fechas no se ha seleccionado, se desactiva el boton aceptar
        if (stComprobarFechaIni.isEmpty() || stComprobarFechaFinal.isEmpty()) {
            btAceptarOtrasActividades.setEnabled(false);
        }
        //Si las fechas estan bien pero está selecionado otrs y no hay si o no, se desactiva el boton aceptar
        else if (rbAsuntos.isChecked()){
            if(!rbRetribuidoSi.isChecked() && !rbRetribuidoNo.isChecked()){
                btAceptarOtrasActividades.setEnabled(false);
            }
            else {btAceptarOtrasActividades.setEnabled(true);}
        //Si está marcado retribuido
        } else if (rbOtros.isChecked()) {
            //Si está marcado el si o el no
            if(rbRetribuidoSi.isChecked() || rbRetribuidoNo.isChecked()){
                //se comprueba que tenga nombre el EditText
                if(!stNombreTrim.equals("")) {
                    //Se activa el botón
                    btAceptarOtrasActividades.setEnabled(true);
                }
                else {

                    btAceptarOtrasActividades.setEnabled(true);}
            }

        }
        //si toddo es correcto se activa el botón aceptar
        else {
            btAceptarOtrasActividades.setEnabled(true);
        }
    }

    //Metodo que comprueba las horas y los minutos del computo
    private void comprobarCamposComputo() {
        //varirable para extraer el texto de la hora y los minutos insertado
        String stHComputo = etHorasComputo.getText().toString();
        String stMComputo = etMinutoComputo.getText().toString();
        if (stHComputo.equals("")) {
            pstHorasComputo = "00";
        } else {
            //variable para pasarlo a numero
            int intHComputo = Integer.parseInt(stHComputo);
            //si la hora es menos de 10, se le pone un cero delante
            pstHorasComputo = (intHComputo < 10)?"0" + stHComputo: stHComputo;
        }  if (stMComputo.equals("")) {
            pstMinutosComputo = "00";
        } else {
            //variable para pasarlo a numero
            int intMComputo = Integer.parseInt(stMComputo);
            //si la hora es menos de 10, se le pone un cero delante
            pstMinutosComputo = (intMComputo < 10)?"0" + stMComputo: stMComputo;
        }
        stTotalHoras = pstHorasComputo + ":" + pstMinutosComputo;
    }

    //metodo para guardar la fecha
    private void guardarFecha(String fechaGuardar) {
        //Metodo para obtener el nombre del dia a guardar
        obtenerNombreDia(fechaGuardar);

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
        String stEstadoADR = "no";
        String ceroHoras = "00:00";
        String ceroEuros = "0,00";
        //Se le pasa al value todos los datos
        values.put(Utilidades.CAMPO_ID, intNumeroDeRegistros);

        values.put(Utilidades.CAMPO_FECHA,fechaGuardar );//guardar la fecha completa
        values.put(Utilidades.CAMPO_OTRAS_ACTIVIDADES,stRBpulsado);//el nombre de las otras actividades
        values.put(Utilidades.CAMPO_OTRAS_ACTIVIDADES_RETRIBUIDO,stRetribuido);//retribuido si o no
        values.put(Utilidades.CAMPO_DIA, stNombreDelDia);//el nombre del dia
        values.put(Utilidades.CAMPO_ADR, stEstadoADR);//el radio botón del ADR
        values.put(Utilidades.CAMPO_HORA_INI, ceroHoras);//la hora de inicio
        values.put(Utilidades.CAMPO_HORA_FIN, ceroHoras);//la hora final
        values.put(Utilidades.CAMPO_TOTAL_HORAS, stTotalHoras);//el total de horas trabajadas
        values.put(Utilidades.CAMPO_DESAYUNO, ceroEuros);//el desayuno
        values.put(Utilidades.CAMPO_COMIDA, ceroEuros);//la comida
        values.put(Utilidades.CAMPO_CENA, ceroEuros);//la cena
        values.put(Utilidades.CAMPO_DORMIR, ceroEuros);//la noche
        values.put(Utilidades.CAMPO_TOTAL_DIETAS, ceroEuros);//el total de las dietas

        values.put(Utilidades.CAMPO_EXTRAS,ceroEuros);
        //para las consultas de la db
        //extraer el dia,mes, año que se va a guardar
        values.put(Utilidades.CAMPO_NUM_DIA, stPrivadaNumDia);
        values.put(Utilidades.CAMPO_NUM_MES, stPrivadaNumMes);
        values.put(Utilidades.CAMPO_NUM_YEAR, stPrivadaNumYear);
        Long idResultante = bd.insert(Utilidades.TABLA_FICHAMENSUAL,Utilidades.CAMPO_ID,values);
        bd.close();

    }

    //metodo que borra el form y vuelve a inicio
    private void borrarFormularioOtrasActividades() {
        String cerocero = "00";
        rgSelec.clearCheck();
        rgRetribuido.clearCheck();
        rgRetribuido.setVisibility(View.GONE);
        etNombreOtrasActividades.setText("");
        etNombreOtrasActividades.setHint("Otra actividad");
        tvIniOtrasActividades.setText("");
        lySumarComputo.setVisibility(View.GONE);
        etHorasComputo.setText("");
        etHorasComputo.setText(cerocero);
        etMinutoComputo.setText("");
        etMinutoComputo.setText(cerocero);
        lyBotonInicio.setVisibility(View.GONE);
        tvFinalOtrasActividades.setText("");
        lyBotonFinal.setVisibility(View.GONE);
        btAceptarOtrasActividades.setEnabled(false);
        lyNombreOtraActividad.setVisibility(View.GONE);
        stNombreTrim = "";
        stRBpulsado = "";
        stRBpulsadoOtros = "";
        stRetribuido = "";
        //volver a inicio
        Fragment fragmentVolveInicio =new Inicio();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragmentVolveInicio);
        transaction.addToBackStack(null);
        transaction.commit();
    }




    /* ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ METODOS FIN ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲ */


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
    public void onPause() {
        super.onPause();
        ///////////////GuardarDatosShare();
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
