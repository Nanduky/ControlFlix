package com.nanduky.controlflix;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nanduky.controlflix.utilidades.Utilidades;
import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;

import java.util.Objects;

/*Fragmento donde eliges el mes del año seleccionado para ver los dias que hay trabajados*/
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArchivoConsultas.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArchivoConsultas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivoConsultas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArchivoConsultas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchivoConsultas.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivoConsultas newInstance(String param1, String param2) {
        ArchivoConsultas fragment = new ArchivoConsultas();
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
    //Crear el View para conectar las casillas, botones, etc...
    View vista;


    /** Crear conexión con la base de datos **/
    //el nombre de la base de datos será db_fichaMensual
    ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);


    /* ****************************   NUEVOS ACCEOS FIN  ***************************************** */
    //layout contenedor padre, hijos mes y dia
    LinearLayout lyConsultasPadre, lyConsultasHijoMes,lyConsultasHijoDias;
    //tabletlayout que contendrá los cuatro trimestres
    TableLayout tlAnuario;
    //table row que contendrá la cabecera con el año y los bloques de los trimestres
    TableRow trContenedorYear, trTrimestre1, trTrimestre2, trTrimestre3, trTrimestre4;
    //los textView de los 12 meses
    TextView tvYear, tvAtras, tvEnero, tvFebrero, tvMarzo, tvAbril, tvMayo, tvJunio,
            tvJulio, tvAgosto, tvSeptiembre, tvOctubre, tvNoviembre, tvDiciembre, vuelveAtras;
    //el ScroolView para canviar el color de fondo
    ScrollView scrollArchivoConsulta;


    //variable con los numeros de dias
    private int intEnero, intFebrero, intMarzo, intAbril, intMayo, intJunio, intJulio, intAgosto,
            intSeptiembre, intOctubre, intNoviembre, intDiciembre;
    /* *********************************   CONSULTORIO   ******************************************/


    String qSeletTodo = "SELECT * FROM ";
    String qSelectCampo = "SELECT (";
    String qCuentaTodo = "SELECT COUNT(*) FROM ";
    String qFrom = ") FROM ";
    String qWhere = " WHERE ";
    String qAnd = " AND ";
    String qOrderBy = " ORDER BY";



    /* *********************************   CONSULTORIO FIN  ***************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_archivo_consultas, container, false);
        /* ***************************  ON CREATE  ********************************************* */
        //inflar los layauts
        lyConsultasPadre = vista.findViewById(R.id.lyConsultasPadre);
        lyConsultasHijoMes  = vista.findViewById(R.id.lyConsultasHijoMeses);
        lyConsultasHijoDias  = vista.findViewById(R.id.lyConsultasHijoDias);

        //inflar el Scrool
        scrollArchivoConsulta = vista.findViewById(R.id.scrollArchivoConsulta);
        //cuando se gira la pantalla se le dice que vuelva a cargar la hoja mensual
        if(savedInstanceState != null){
            stYearSelect = savedInstanceState.getString("Year_Seleccionado");
            Bundle pasaYear = new Bundle();
            pasaYear.putString("stYearSelect", stYearSelect);
            //volver a inicio
            Fragment fragmentArchivoConsulta =new ArchivoConsultas();
            fragmentArchivoConsulta.setArguments(pasaYear);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragmentArchivoConsulta);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else{
            //Metodo para ver los meses del año elegido
            mesYearSelect();
        }





        /* ***************************  ON CREATE FIN ****************************************** */
        return vista;
    }

    //Variable para guardar el año para pasarlo al archivo ver dia
    private String stYearSelect = "";
    //Metodo para ver los meses del año elegido
    private void mesYearSelect() {
        //recoger el paquete que viene de Archivo
         Bundle pasaYear = this.getArguments();
        //cambiar el color de fondo del scrollView
        scrollArchivoConsulta.setBackgroundColor(Color.parseColor("#FFEB3B"));
        //Comprobar que el paquete no llegue vacio
        if(pasaYear != null){
            //sacar el sobre
            stYearSelect = pasaYear.getString(("stYearSelect"));
            //comprovar que el sobre no esté vacio
            if(stYearSelect != null){
                //crear los parametros padre
                LinearLayout.LayoutParams params_ly_mat_wra = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                //crear los parametros hijo mes
                LinearLayout.LayoutParams params_ly_mat_mat = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                //crear los parametros de los textView
                LinearLayout.LayoutParams params_ly_wra_wra_marg_1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params_ly_wra_wra_marg_1.setMargins(1,1,1,1);

                //crear los parametros para el anuario
                TableLayout.LayoutParams params_tl_mat_mat = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                //crear los parametros del TableRow
                TableRow.LayoutParams params_tr_mat_mat_wei_1 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                params_tr_mat_mat_wei_1.weight = 1;
                TableRow.LayoutParams params_tr_wra_wra_wei_1 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                params_tr_mat_mat_wei_1.weight = 1;

                //crear parametros de los trimestres
                TableRow.LayoutParams params_tr_mat_mat_wei_marg_1 = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                params_tr_mat_mat_wei_marg_1.weight = 1;
                params_tr_mat_mat_wei_marg_1.setMargins(1,1,1,1);



                /* *************************** Zona contenedor padre  *************************************************************** */

                //contenedor padre
//                lyConsultasPadre = new LinearLayout(getActivity());
                lyConsultasPadre.setOrientation(LinearLayout.VERTICAL);
                lyConsultasPadre.setWeightSum(2);


                /* *************************** Zona contenedor hijo mes  ************************************************** */

                //contenedor hijo mes
                lyConsultasHijoMes = new LinearLayout(getActivity());
                lyConsultasHijoMes.setBackgroundColor(Color.parseColor("#FFEB3B"));
                lyConsultasHijoMes.setPadding(5,5,5,5);
                lyConsultasHijoMes.setOrientation(LinearLayout.HORIZONTAL);


                /* *************************** Zona anuario  ************************************************ */

                //el anuario
                tlAnuario = new TableLayout(getActivity());
                tlAnuario.setWeightSum(5);

                /* *************************** Zona Cabecera año ******************************** */

                //el TableRow año
                trContenedorYear = new TableRow(getActivity());
                trContenedorYear.setWeightSum((float) 3);
                trContenedorYear.setGravity(Gravity.NO_GRAVITY);
                //trContenedorYear.setBackgroundColor(Color.CYAN);


                //meter el año y los trimestres en el anuario

                //el atras
                tvAtras = new TextView(getActivity());
                tvAtras.setPadding(5,8,5,8);
                tvAtras.setLayoutParams(params_ly_wra_wra_marg_1);
                tvAtras.setTextColor(Color.parseColor("#FFFFFF"));
                tvAtras.setBackgroundColor(Color.parseColor("#000000"));
                tvAtras.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvAtras.setTextSize((float)32);
                tvAtras.setTypeface(Typeface.DEFAULT_BOLD);
                String stVolverAtras = " VOLVER ";
                tvAtras.isClickable();
                tvAtras.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Metodo para volver atras Archivo.clas (el año a elegir)
                        volverAyears();
                    }
                });
                tvAtras.setText(stVolverAtras);
                //el contenedor del spacio vacio
                trContenedorYear.addView(tvAtras, params_tr_wra_wra_wei_1);
                tvAtras = new TextView(getActivity());
                tvAtras.setPadding(5,8,5,8);
                tvAtras.setLayoutParams(params_ly_wra_wra_marg_1);
                tvAtras.setTextColor(Color.parseColor("#FFFFFF"));
                tvAtras.setBackgroundColor(Color.parseColor("#FFEB3B"));
                tvAtras.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvAtras.setTextSize((float)32);
                tvAtras.setTypeface(Typeface.DEFAULT_BOLD);
                tvAtras.setText(" ");
                //el contenedor del año
                trContenedorYear.addView(tvAtras, params_tr_wra_wra_wei_1);



                //el año
                tvYear = new TextView(getActivity());
                tvYear.setPadding(5,8,5,8);
                tvYear.setLayoutParams(params_tr_mat_mat_wei_marg_1);
                tvYear.setTextColor(Color.parseColor("#FFFFFF"));
                tvYear.setBackgroundColor(Color.parseColor("#000000"));
                tvYear.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvYear.setTextSize((float)32);
                tvYear.setTypeface(Typeface.DEFAULT_BOLD);
                tvYear.setText(stYearSelect);
                //el contenedor del año
                trContenedorYear.addView(tvYear, params_tr_wra_wra_wei_1);

                tlAnuario.addView(trContenedorYear,params_tr_mat_mat_wei_marg_1);

                /* *************************** Zona Cabecera año fin  *************************** */

                /* ************* crear consulta de los meses del año seleccionda *************** */
                //contar todos los dias del mes del años seleccionado
                String queryEnero = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"01\"";
                String queryFebrero = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"02\"";
                String queryMarzo = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"03\"";
                String queryAbril = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"04\"";
                String queryMayo = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"05\"";
                String queryJunio = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"06\"";
                String queryJulio = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"07\"";
                String queryAgosto = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"08\"";
                String querySeptiembre = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"09\"";
                String queryoctubre = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"10\"";
                String queryNovienbre = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"11\"";
                String queryDiciembre = qCuentaTodo + Utilidades.TABLA_FICHAMENSUAL + qWhere
                        + Utilidades.CAMPO_NUM_YEAR + " = " + stYearSelect + qAnd
                        + Utilidades.CAMPO_NUM_MES + " = \"12\"";

                /* ************* crear consulta de los meses del año seleccionda *************** */
                /** Crear conexión con la base de datos **/
                //el nombre de la base de datos será db_fichaMensual
                ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
                //Crear la conexion con la base de datos
                SQLiteDatabase db = conn.getReadableDatabase();
                String stEnero = "0", stFebrero = "0", stMarzo = "0", stAbril = "0", stMayo = "0",
                        stJunio = "0", stJulio = "0", stAgosto = "0", stSeptiembre = "0",
                        stOctubre = "0",stNoviembre = "0", stDiciembre = "0";

                try {
                    //contar el numero de dias trabajados
                    stEnero      = DatabaseUtils.stringForQuery(db, queryEnero, null);
                    stFebrero    = DatabaseUtils.stringForQuery(db, queryFebrero, null);
                    stMarzo      = DatabaseUtils.stringForQuery(db, queryMarzo, null);
                    stAbril      = DatabaseUtils.stringForQuery(db, queryAbril, null);
                    stMayo       = DatabaseUtils.stringForQuery(db, queryMayo, null);
                    stJunio      = DatabaseUtils.stringForQuery(db, queryJunio, null);
                    stJulio      = DatabaseUtils.stringForQuery(db, queryJulio, null);
                    stAgosto     = DatabaseUtils.stringForQuery(db, queryAgosto, null);
                    stSeptiembre = DatabaseUtils.stringForQuery(db, querySeptiembre, null);
                    stOctubre    = DatabaseUtils.stringForQuery(db, queryoctubre, null);
                    stNoviembre  = DatabaseUtils.stringForQuery(db, queryNovienbre, null);
                    stDiciembre  = DatabaseUtils.stringForQuery(db, queryDiciembre, null);
                    intEnero = Integer.parseInt(stEnero);
                    intFebrero = Integer.parseInt(stFebrero);
                    intMarzo = Integer.parseInt(stMarzo);
                    intAbril = Integer.parseInt(stAbril);
                    intMayo = Integer.parseInt(stMayo);
                    intJunio = Integer.parseInt(stJunio);
                    intJulio = Integer.parseInt(stJulio);
                    intAgosto = Integer.parseInt(stAgosto);
                    intSeptiembre = Integer.parseInt(stSeptiembre);
                    intOctubre = Integer.parseInt(stOctubre);
                    intNoviembre = Integer.parseInt(stNoviembre);
                    intDiciembre = Integer.parseInt(stDiciembre);
                } catch (NumberFormatException e) {
                    intEnero = 0; intFebrero = 0; intMarzo = 0; intAbril = 0; intMayo = 0;
                    intJunio = 0; intJulio = 0; intAgosto = 0; intSeptiembre = 0; intOctubre = 0;
                    intNoviembre = 0; intDiciembre = 0;
                }


                /* *************************** Zona trimestre uno  ****************************** */

                //primer
                //La cabecera de cada mes con sus saltos de linea y declarar la variable con la cabecera y el texto (podria estar vacio)
                String st_Enero      = "01\nEnero\n\n";      String st_dia_Enero      = stEnero      + " días trabajados";
                String st_Febrero    = "02\nFebrero\n\n";    String st_dia_Febrero    = stFebrero    + " días trabajados";
                String st_Marzo      = "03\nMarzo\n\n";      String st_dia_Marzo      = stMarzo      + " días trabajados";
                String st_Abril      = "04\nAbril\n\n";      String st_dia_Abril      = stAbril      + " días trabajados";
                String st_Mayo       = "05\nMayo\n\n";       String st_dia_Mayo       = stMayo       + " días trabajados";
                String st_Junio      = "06\nJunio\n\n";      String st_dia_Junio      = stJunio      + " días trabajados";
                String st_Julio      = "07\nJulio\n\n";      String st_dia_Julio      = stJulio      + " días trabajados";
                String st_Agosto     = "08\nAgosto\n\n";     String st_dia_Agosto     = stAgosto     + " días trabajados";
                String st_Septiembre = "09\nSeptiembre\n\n"; String st_dia_Septiembre = stSeptiembre + " días trabajados";
                String st_Octubre    = "10\nOctubre\n\n";    String st_dia_Octubre    = stOctubre    + " días trabajados";
                String st_Noviembre  = "11\nNoviembre\n\n";  String st_dia_Noviembre  = stNoviembre  + " días trabajados";
                String st_Diciembre  = "12\nDiciembre\n\n";  String st_dia_Diciembre  = stDiciembre  + " días trabajados";

                //se compueba si no hay días trabajados y se pone un "vacio" en el caso de que sea verdad
                if(intEnero      == 0){st_dia_Enero      = " - Vacío -";} else if(intEnero      >= 1){st_dia_Enero      = stEnero      + " día trabajado";}
                if(intFebrero    == 0){st_dia_Febrero    = " - Vacío -";} else if(intFebrero    >= 1){st_dia_Febrero    = stFebrero    + " día trabajado";}
                if(intMarzo      == 0){st_dia_Marzo      = " - Vacío -";} else if(intMarzo      >= 1){st_dia_Marzo      = stMarzo      + " día trabajado";}
                if(intAbril      == 0){st_dia_Abril      = " - Vacío -";} else if(intAbril      >= 1){st_dia_Abril      = stAbril      + " día trabajado";}
                if(intMayo       == 0){st_dia_Mayo       = " - Vacío -";} else if(intMayo       >= 1){st_dia_Mayo       = stMayo       + " día trabajado";}
                if(intJunio      == 0){st_dia_Junio      = " - Vacío -";} else if(intJunio      >= 1){st_dia_Junio      = stJunio      + " día trabajado";}
                if(intJulio      == 0){st_dia_Julio      = " - Vacío -";} else if(intJulio      >= 1){st_dia_Julio      = stJulio      + " día trabajado";}
                if(intAgosto     == 0){st_dia_Agosto     = " - Vacío -";} else if(intAgosto     >= 1){st_dia_Agosto     = stAgosto     + " día trabajado";}
                if(intSeptiembre == 0){st_dia_Septiembre = " - Vacío -";} else if(intSeptiembre >= 1){st_dia_Septiembre = stSeptiembre + " día trabajado";}
                if(intOctubre    == 0){st_dia_Octubre    = " - Vacío -";} else if(intOctubre    >= 1){st_dia_Octubre    = stOctubre    + " día trabajado";}
                if(intNoviembre  == 0){st_dia_Noviembre  = " - Vacío -";} else if(intNoviembre  >= 1){st_dia_Noviembre  = stNoviembre  + " día trabajado";}
                if(intDiciembre  == 0){st_dia_Diciembre  = " - Vacío -";} else if(intDiciembre  >= 1){st_dia_Diciembre  = stDiciembre  + " día trabajado";}


                //los colores que se mostraran las letras de los cuadros de los meses según hayan dias
                //trabajados o no
                int mesConDias =  Color.parseColor("#FFFFFF");//blanco
                int mesSinDias =  Color.parseColor("#FF6D6D69");//gris
                int colorTextoMesEnero = 0, colorTextoMesFebrero = 0, colorTextoMesMarzo = 0,
                        colorTextoMesAbril = 0, colorTextoMesMayo = 0, colorTextoMesJunio = 0,
                        colorTextoMesJulio = 0, colorTextoMesAgosto = 0, colorTextoMesSeptiembre = 0,
                        colorTextoMesOctubre = 0, colorTextoMesNoviembre = 0, colorTextoMesDiciembre = 0;

                if(intEnero      == 0){colorTextoMesEnero      = mesSinDias;} else if(intEnero      >= 1){colorTextoMesEnero      = mesConDias;}
                if(intFebrero    == 0){colorTextoMesFebrero    = mesSinDias;} else if(intFebrero    >= 1){colorTextoMesFebrero    = mesConDias;}
                if(intMarzo      == 0){colorTextoMesMarzo      = mesSinDias;} else if(intMarzo      >= 1){colorTextoMesMarzo      = mesConDias;}
                if(intAbril      == 0){colorTextoMesAbril      = mesSinDias;} else if(intAbril      >= 1){colorTextoMesAbril      = mesConDias;}
                if(intMayo       == 0){colorTextoMesMayo       = mesSinDias;} else if(intMayo       >= 1){colorTextoMesMayo       = mesConDias;}
                if(intJunio      == 0){colorTextoMesJunio      = mesSinDias;} else if(intJunio      >= 1){colorTextoMesJunio      = mesConDias;}
                if(intJulio      == 0){colorTextoMesJulio      = mesSinDias;} else if(intJulio      >= 1){colorTextoMesJulio      = mesConDias;}
                if(intAgosto     == 0){colorTextoMesAgosto     = mesSinDias;} else if(intAgosto     >= 1){colorTextoMesAgosto     = mesConDias;}
                if(intSeptiembre == 0){colorTextoMesSeptiembre = mesSinDias;} else if(intSeptiembre >= 1){colorTextoMesSeptiembre = mesConDias;}
                if(intOctubre    == 0){colorTextoMesOctubre    = mesSinDias;} else if(intOctubre    >= 1){colorTextoMesOctubre    = mesConDias;}
                if(intNoviembre  == 0){colorTextoMesNoviembre  = mesSinDias;} else if(intNoviembre  >= 1){colorTextoMesNoviembre  = mesConDias;}
                if(intDiciembre  == 0){colorTextoMesDiciembre  = mesSinDias;} else if(intDiciembre  >= 1){colorTextoMesDiciembre  = mesConDias;}

                //Para cambiar el tamaño del texto en un mismo TextView
                Spannable spanEnero      = new SpannableString(st_Enero      + st_dia_Enero);
                Spannable spanFebrero    = new SpannableString(st_Febrero    + st_dia_Febrero);
                Spannable spanMarzo      = new SpannableString(st_Marzo      + st_dia_Marzo);
                Spannable spanAbril      = new SpannableString(st_Abril      + st_dia_Abril);
                Spannable spanMayo       = new SpannableString(st_Mayo       + st_dia_Mayo);
                Spannable spanJunio      = new SpannableString(st_Junio      + st_dia_Junio);
                Spannable spanJulio      = new SpannableString(st_Julio      + st_dia_Julio);
                Spannable spanAgosto     = new SpannableString(st_Agosto     + st_dia_Agosto);
                Spannable spanSeptiembre = new SpannableString(st_Septiembre + st_dia_Septiembre);
                Spannable spanOctubre    = new SpannableString(st_Octubre    + st_dia_Octubre);
                Spannable spanNoviembre  = new SpannableString(st_Noviembre  + st_dia_Noviembre);
                Spannable spanDiciembre  = new SpannableString(st_Diciembre  + st_dia_Diciembre);

                spanEnero.setSpan(     new RelativeSizeSpan(1.8f),0,st_Enero.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanFebrero.setSpan(   new RelativeSizeSpan(1.8f),0,st_Febrero.length(),    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanMarzo.setSpan(     new RelativeSizeSpan(1.8f),0,st_Marzo.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanAbril.setSpan(     new RelativeSizeSpan(1.8f),0,st_Abril.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanMayo.setSpan(      new RelativeSizeSpan(1.8f),0,st_Mayo.length(),       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanJunio.setSpan(     new RelativeSizeSpan(1.8f),0,st_Junio.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanJulio.setSpan(     new RelativeSizeSpan(1.8f),0,st_Julio.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanAgosto.setSpan(    new RelativeSizeSpan(1.8f),0,st_Agosto.length(),     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanSeptiembre.setSpan(new RelativeSizeSpan(1.8f),0,st_Septiembre.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanOctubre.setSpan(   new RelativeSizeSpan(1.8f),0,st_Octubre.length(),    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanNoviembre.setSpan( new RelativeSizeSpan(1.8f),0,st_Noviembre.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanDiciembre.setSpan( new RelativeSizeSpan(1.8f),0,st_Diciembre.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                spanEnero.setSpan(     new RelativeSizeSpan(0.8f),st_Enero.length(),
                        st_Enero.length()      + st_dia_Enero.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanFebrero.setSpan(   new RelativeSizeSpan(0.8f),st_Febrero.length(),
                        st_Febrero.length()    + st_dia_Febrero.length(),    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanMarzo.setSpan(     new RelativeSizeSpan(0.8f),st_Marzo.length(),
                        st_Marzo.length()      + st_dia_Marzo.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanAbril.setSpan(     new RelativeSizeSpan(0.8f),st_Abril.length(),
                        st_Abril.length()      + st_dia_Abril.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanMayo.setSpan(      new RelativeSizeSpan(0.8f),st_Mayo.length(),
                        st_Mayo.length()       + st_dia_Mayo.length(),       Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanJunio.setSpan(     new RelativeSizeSpan(0.8f),st_Junio.length(),
                        st_Junio.length()      + st_dia_Junio.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanJulio.setSpan(     new RelativeSizeSpan(0.8f),st_Julio.length(),
                        st_Julio.length()      + st_dia_Julio.length(),      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanAgosto.setSpan(    new RelativeSizeSpan(0.8f),st_Agosto.length(),
                        st_Agosto.length()     + st_dia_Agosto.length(),     Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanSeptiembre.setSpan(new RelativeSizeSpan(0.8f),st_Septiembre.length(),
                        st_Septiembre.length() + st_dia_Septiembre.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanOctubre.setSpan(   new RelativeSizeSpan(0.8f),st_Octubre.length(),
                        st_Octubre.length()    + st_dia_Octubre.length(),    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanNoviembre.setSpan( new RelativeSizeSpan(0.8f),st_Noviembre.length(),
                        st_Noviembre.length()  + st_dia_Noviembre.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanDiciembre.setSpan( new RelativeSizeSpan(0.8f),st_Diciembre.length(),
                        st_Diciembre.length()  + st_dia_Diciembre.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                //Cambiar el color del texto según hayan días o no en el mes

                //el TableRow del primer trimestre
                trTrimestre1 = new TableRow(getActivity());
                trTrimestre1.setWeightSum((float)3);
                trTrimestre1.setGravity(Gravity.CENTER);

                //Enero
                tvEnero = new TextView(getActivity());
                tvEnero.setPadding(5,8,5,8);
                tvEnero.setLayoutParams(params_ly_wra_wra_marg_1);
                tvEnero.setTextColor(colorTextoMesEnero);
                tvEnero.setBackgroundColor(Color.parseColor("#000000"));
                tvEnero.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvEnero.setTypeface(Typeface.DEFAULT_BOLD);
                tvEnero.setText(spanEnero);
                tvEnero.isClickable();
                tvEnero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(intEnero != 0)
                         MostrarMes("enero",stYearSelect);
                    }
                });
                trTrimestre1.addView(tvEnero,params_tr_mat_mat_wei_marg_1);

                //Febrero
                tvFebrero = new TextView(getActivity());
                tvFebrero.setPadding(5,8,5,8);
                tvFebrero.setLayoutParams(params_ly_wra_wra_marg_1);
                tvFebrero.setTextColor(colorTextoMesFebrero);
                tvFebrero.setBackgroundColor(Color.parseColor("#000000"));
                tvFebrero.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvFebrero.setTypeface(Typeface.DEFAULT_BOLD);
                tvFebrero.setText(spanFebrero);
                tvFebrero.isClickable();
                tvFebrero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intFebrero != 0) {
                            MostrarMes("febrero", stYearSelect);
                        }
                    }
                });
                trTrimestre1.addView(tvFebrero,params_tr_mat_mat_wei_marg_1);

                //Marzo
                tvMarzo = new TextView(getActivity());
                tvMarzo.setPadding(5,8,5,8);
                tvMarzo.setLayoutParams(params_ly_wra_wra_marg_1);
                tvMarzo.setTextColor(colorTextoMesMarzo);
                tvMarzo.setBackgroundColor(Color.parseColor("#000000"));
                tvMarzo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvMarzo.setTypeface(Typeface.DEFAULT_BOLD);
                tvMarzo.setText(spanMarzo);
                tvMarzo.isClickable();
                tvMarzo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intMarzo != 0) {
                            MostrarMes("marzo", stYearSelect);
                        }
                    }
                });
                trTrimestre1.addView(tvMarzo,params_tr_mat_mat_wei_marg_1);

                //meter el año y los trimestres en el anuario
                tlAnuario.addView(trTrimestre1,params_tr_mat_mat_wei_marg_1);

                /* *************************** Zona trimestre uno fin *************************** */

                /* *************************** Zona trimestre dos  ****************************** */

                //el TableRow del primer trimestre
                trTrimestre2 = new TableRow(getActivity());
                trTrimestre2.setWeightSum((float)3);
                trTrimestre2.setGravity(Gravity.CENTER);

                //Abril
                tvAbril = new TextView(getActivity());
                tvAbril.setPadding(5,8,5,8);
                tvAbril.setLayoutParams(params_ly_wra_wra_marg_1);
                tvAbril.setTextColor(colorTextoMesAbril);
                tvAbril.setBackgroundColor(Color.parseColor("#000000"));
                tvAbril.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvAbril.setTypeface(Typeface.DEFAULT_BOLD);
                tvAbril.setText(spanAbril);
                tvAbril.isClickable();
                tvAbril.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intAbril != 0) {
                            MostrarMes("abril", stYearSelect);
                        }
                    }
                });
                trTrimestre2.addView(tvAbril,params_tr_mat_mat_wei_marg_1);

                //Mayo
                tvMayo = new TextView(getActivity());
                tvMayo.setPadding(5,8,5,8);
                tvMayo.setLayoutParams(params_ly_wra_wra_marg_1);
                tvMayo.setTextColor(colorTextoMesMayo);
                tvMayo.setBackgroundColor(Color.parseColor("#000000"));
                tvMayo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvMayo.setTypeface(Typeface.DEFAULT_BOLD);
                tvMayo.setText(spanMayo);
                tvMayo.isClickable();
                tvMayo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intMayo != 0) {
                            MostrarMes("mayo", stYearSelect);
                        }
                    }
                });
                trTrimestre2.addView(tvMayo,params_tr_mat_mat_wei_marg_1);

                //Junio
                tvJunio = new TextView(getActivity());
                tvJunio.setPadding(5,8,5,8);
                tvJunio.setLayoutParams(params_ly_wra_wra_marg_1);
                tvJunio.setTextColor(colorTextoMesJunio);
                tvJunio.setBackgroundColor(Color.parseColor("#000000"));
                tvJunio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvJunio.setTypeface(Typeface.DEFAULT_BOLD);
                tvJunio.setText(spanJunio);
                tvJunio.isClickable();
                tvJunio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intJunio != 0) {
                            MostrarMes("junio", stYearSelect);
                        }
                    }
                });
                trTrimestre2.addView(tvJunio,params_tr_mat_mat_wei_marg_1);

                //meter el año y los trimestres en el anuario
                tlAnuario.addView(trTrimestre2,params_tr_mat_mat_wei_marg_1);

                /* *************************** Zona trimestre dos fin *************************** */

                /* *************************** Zona trimestre tres  ***************************** */

                //el TableRow del primer trimestre
                trTrimestre3 = new TableRow(getActivity());
                trTrimestre3.setWeightSum((float)3);
                trTrimestre3.setGravity(Gravity.CENTER);

                //julio
                tvJulio = new TextView(getActivity());
                tvJulio.setPadding(5,8,5,8);
                tvJulio.setLayoutParams(params_ly_wra_wra_marg_1);
                tvJulio.setTextColor(colorTextoMesJulio);
                tvJulio.setBackgroundColor(Color.parseColor("#000000"));
                tvJulio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvJulio.setTypeface(Typeface.DEFAULT_BOLD);
                tvJulio.setText(spanJulio);
                tvJulio.isClickable();
                tvJulio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intJulio != 0) {
                            MostrarMes("julio", stYearSelect);
                        }
                    }
                });
                trTrimestre3.addView(tvJulio,params_tr_mat_mat_wei_marg_1);

                //Agosto
                tvAgosto = new TextView(getActivity());
                tvAgosto.setPadding(5,8,5,8);
                tvAgosto.setLayoutParams(params_ly_wra_wra_marg_1);
                tvAgosto.setTextColor(colorTextoMesAgosto);
                tvAgosto.setBackgroundColor(Color.parseColor("#000000"));
                tvAgosto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvAgosto.setTypeface(Typeface.DEFAULT_BOLD);
                tvAgosto.setText(spanAgosto);
                tvAgosto.isClickable();
                tvAgosto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intAgosto != 0) {
                            MostrarMes("agosto", stYearSelect);
                        }
                    }
                });
                trTrimestre3.addView(tvAgosto,params_tr_mat_mat_wei_marg_1);

                //Septiembre
                tvSeptiembre = new TextView(getActivity());
                tvSeptiembre.setPadding(5,8,5,8);
                tvSeptiembre.setLayoutParams(params_ly_wra_wra_marg_1);
                tvSeptiembre.setTextColor(colorTextoMesSeptiembre);
                tvSeptiembre.setBackgroundColor(Color.parseColor("#000000"));
                tvSeptiembre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvSeptiembre.setTypeface(Typeface.DEFAULT_BOLD);
                tvSeptiembre.setText(spanSeptiembre);
                tvSeptiembre.isClickable();
                tvSeptiembre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intSeptiembre != 0) {
                            MostrarMes("septiembre", stYearSelect);
                        }
                    }
                });
                trTrimestre3.addView(tvSeptiembre,params_tr_mat_mat_wei_marg_1);

                //meter el año y los trimestres en el anuario
                tlAnuario.addView(trTrimestre3,params_tr_mat_mat_wei_marg_1);

                /* *************************** Zona trimestre tres fin ************************** */

                /* *************************** Zona trimestre cuatro  *************************** */

                //el TableRow del primer trimestre
                trTrimestre4 = new TableRow(getActivity());
                trTrimestre4.setWeightSum((float)3);
                trTrimestre4.setGravity(Gravity.CENTER);

                //Octubre
                tvOctubre = new TextView(getActivity());
                tvOctubre.setPadding(5,8,5,8);
                tvOctubre.setLayoutParams(params_ly_wra_wra_marg_1);
                tvOctubre.setTextColor(colorTextoMesOctubre);
                tvOctubre.setBackgroundColor(Color.parseColor("#000000"));
                tvOctubre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvOctubre.setTypeface(Typeface.DEFAULT_BOLD);
                tvOctubre.setText(spanOctubre);
                tvOctubre.isClickable();
                tvOctubre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intOctubre != 0) {
                            MostrarMes("octubre", stYearSelect);
                        }
                    }
                });
                trTrimestre4.addView(tvOctubre,params_tr_mat_mat_wei_marg_1);

                //Noviembre
                tvNoviembre = new TextView(getActivity());
                tvNoviembre.setPadding(5,8,5,8);
                tvNoviembre.setLayoutParams(params_ly_wra_wra_marg_1);
                tvNoviembre.setTextColor(colorTextoMesNoviembre);
                tvNoviembre.setBackgroundColor(Color.parseColor("#000000"));
                tvNoviembre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvNoviembre.setTypeface(Typeface.DEFAULT_BOLD);
                tvNoviembre.setText(spanNoviembre);
                tvNoviembre.isClickable();
                tvNoviembre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intNoviembre != 0) {
                            MostrarMes("noviembre", stYearSelect);
                        }
                    }
                });
                trTrimestre4.addView(tvNoviembre,params_tr_mat_mat_wei_marg_1);

                //Diciembre
                tvDiciembre = new TextView(getActivity());
                tvDiciembre.setPadding(5,8,5,8);
                tvDiciembre.setLayoutParams(params_ly_wra_wra_marg_1);
                tvDiciembre.setTextColor(colorTextoMesDiciembre);
                tvDiciembre.setBackgroundColor(Color.parseColor("#000000"));
                tvDiciembre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvDiciembre.setTypeface(Typeface.DEFAULT_BOLD);
                tvDiciembre.setText(spanDiciembre);
                tvDiciembre.isClickable();
                tvDiciembre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (intDiciembre != 0) {
                            MostrarMes("diciembre", stYearSelect);
                        }
                    }
                });
                trTrimestre4.addView(tvDiciembre,params_tr_mat_mat_wei_marg_1);

                //meter el año y los trimestres en el anuario
                tlAnuario.addView(trTrimestre4,params_tr_mat_mat_wei_marg_1);

                /* *************************** Zona trimestre cuatro fin ************************ */


                //meter el año y los trimestres en el anuario
               ///// tlAnuario.addView(trContenedorYear,parametrosAnuario);
                /* *************************** Zona anuario fin ********************************************* */
                //meter el anuario en el contenedor hijo mes
                lyConsultasHijoMes.addView(tlAnuario,params_ly_mat_mat);
                /* *************************** Zona contenedor hijo mes fin *********************************************** */
                //meterlo todos en la consulta padre
                lyConsultasPadre.addView(lyConsultasHijoMes,params_ly_mat_wra);
                lyConsultasHijoDias.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(getContext(),"año: 0 seleccionado",Toast.LENGTH_SHORT).show();
            }

            /* *************************** Zona contenedor padre fin ************************************************************ */
        }
     }



    //Metodo para mostrar los dias del mes y ocultar los meses
    private void MostrarMes(String mesSelect,String stYear) {


        //Bundle el mensajero
        Bundle bunPasarMesYear = new Bundle();
        //el paquete con el contenido
        bunPasarMesYear.putString("stYearSelect", stYear);
        bunPasarMesYear.putString("stMesSelect", mesSelect);
        //la direccion de envio
        Fragment nuevoFragmento = new ArchivoConsultasVerDias();
        //se le pone al paquete la direccion
        nuevoFragmento.setArguments(bunPasarMesYear);
        //en caso de que el paquete esté vacio
        assert getFragmentManager() != null;
        //se escriben las instrucciones de donde y como tienen que ir
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //se ejecutan las instrucciones
        transaction.replace(R.id.content_main, nuevoFragmento);//ArchivoConsultaVerDias
        //esto no tiene servicio, es el undo
        transaction.addToBackStack(null);
        //se envia el paquete
        transaction.commit();

    }

    private void volverAyears() {

        //crear el bundle
        Bundle volverAtrasYear = new Bundle();
        //Crear el fragment
        Fragment fragmentVolverYear = new Archivo();
        //pasar el bulde como arguneto
        fragmentVolverYear.setArguments(volverAtrasYear);
        //que no sea nulo
        assert getFragmentManager() != null;
        //Transicion
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //remplazo
        transaction.replace(R.id.content_main,fragmentVolverYear);
        //el undo
        transaction.addToBackStack(null);
        //ir
        transaction.commit();
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
        outState.putString("Year_Seleccionado",stYearSelect);
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
