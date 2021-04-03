package com.nanduky.controlflix;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.nanduky.controlflix.utilidades.Utilidades;
import static com.nanduky.controlflix.utilidades.VariablesGlobales.versionDb;

/*Fragmento donde se selecciona el año que quieres ver los meses que hay*/

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Archivo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Archivo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Archivo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Archivo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Archivo.
     */
    // TODO: Rename and change types and number of parameters
    public static Archivo newInstance(String param1, String param2) {
        Archivo fragment = new Archivo();
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

    //crear un LinealLayour
    private LinearLayout lyContArchivo;

    /** Crear conexión con la base de datos **/


    //el nombre de la base de datos será db_fichaMensual
    ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);


    /* ****************************   NUEVOS ACCEOS FIN  ***************************************** */

    /* *********************************   CONSULTORIO   ******************************************/

    //contar los años distintos que hay
    String queryNumeroDeYearsDistintos = "SELECT COUNT(DISTINCT " + Utilidades.CAMPO_NUM_YEAR + ") FROM "
            + Utilidades.TABLA_FICHAMENSUAL;
    //los años diferentes
    String queryYearsDistintos = "SELECT DISTINCT " + Utilidades.CAMPO_NUM_YEAR + " FROM "
            + Utilidades.TABLA_FICHAMENSUAL + " ORDER BY " + Utilidades.CAMPO_NUM_YEAR + " DESC";
    //todos los datos de la db
    String queryTodosLosDatos = "SELECT * FROM " + Utilidades.TABLA_FICHAMENSUAL;

    /* *********************************   CONSULTORIO FIN  ***************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_archivo, container, false);
        /* ***************************  ON CREATE  ********************************************* */
        //cuando se gira la pantalla se le dice que vuelva a cargar la hoja mensual
        if(savedInstanceState != null){
            //volver a inicio
            Fragment fragmentArchivo =new Archivo();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, fragmentArchivo);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        //implementar el lineal layout
        lyContArchivo = vista.findViewById(R.id.lyContArchivo);


        //Metodo que consultará a la base de datos todos los años diferentes que hay guardados
        consultaNumeroDeYearsDiferentes();

        /* ***************************  ON CREATE FIN ****************************************** */
        return vista;
    }




    /* ***************************  METODOS  ********************************************* */

    //Metodo que comprueba y muestra los diferentes años que hay en la base de datos
    private void consultaNumeroDeYearsDiferentes() {
        //conectar con la db
        ConexionSQliteHelper conn  = new ConexionSQliteHelper(getActivity(),"db_fichaMensual",null,versionDb);
        SQLiteDatabase db = conn.getReadableDatabase();
        //variables para guardar el numero de años diferentes
        int intNumeroDeYearsDiferents = 0;
        try{
            //pasar la consulta y recoger el dato
            intNumeroDeYearsDiferents = Integer.parseInt(DatabaseUtils.stringForQuery(db, queryNumeroDeYearsDistintos,null));
        }catch (NumberFormatException ignored){}


        //crear un cursor para mandar la consulta y recoger los datos y recorrerlos
        Cursor cursorNumeroDeYears = db.rawQuery(queryYearsDistintos,null);

        //comprobar que no esté vacio el cursor
        if(cursorNumeroDeYears != null){
            //mover el cursor al principio
            cursorNumeroDeYears.moveToFirst();

            //crear los textView que contendrán los años diferentes
            //cear los parametros
            LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            parametros.setMargins(8,1,8,1); //izquierda arriba derecha abajo
            parametros.gravity = Gravity.CENTER_HORIZONTAL;
            //Crear el TextView
            TextView textViewYears;

            //bucle para recorre la db
            for(int i = 0; i < intNumeroDeYearsDiferents; i++){
                //implemenntar el TextView
                textViewYears = new TextView(getActivity());
                //pasarle un texto.
                textViewYears.setText(cursorNumeroDeYears.getString(0));
                //el tamaño del texto
                textViewYears.setTextSize((float)40);
                //el color del texto
                textViewYears.setTextColor(Color.parseColor("#FFFFFF"));
                //negrita
                textViewYears.setTypeface(Typeface.DEFAULT_BOLD);
                //el padding
                textViewYears.setPadding(2,2,2,2);
                //alineación del texto
                textViewYears.setGravity(Gravity.CENTER_HORIZONTAL);
                //hacerlo clickable
                textViewYears.isClickable();
                //variable con el año
                final String stYearSelec = cursorNumeroDeYears.getString(0);
                textViewYears.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Bundle el mensajero
                        Bundle pasaYear = new Bundle();
                        //el paquete con el contenido
                        pasaYear.putString("stYearSelect", stYearSelec);
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

 /*                     NO BORRAR. METODO PARA PASAR ENTRE FRAGMENT POR MAINACTIVITY


                    //ir al la actividad MainActivity.java----// Archivo.java
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        //para enviar datos de una actividad a otra, se crea un paquete
                        Bundle miPaqueteYear= new Bundle();
                        //se le dá un valor al bundel, seria meter algo en el paquete
                        miPaqueteYear.putString("miYearSeleccionado",stYearSelec);
                        //se mete el bundel en el inten. seria darle el paquete al mensajero
                        intent.putExtras(miPaqueteYear);
                        //se llama a la actividad
                        startActivity(intent); **/

                    }
                });
                //insertar el TextView
                lyContArchivo.addView(textViewYears,parametros);

                //creat el TextView separador
                textViewYears = new TextView(getActivity());
                //color de fondo
                textViewYears.setBackgroundColor(Color.parseColor("#FFEB3B"));
                //el padding
                textViewYears.setPadding(2,-13,2,-14);
                //Agregar el TextView al Lineal Layaut
                lyContArchivo.addView(textViewYears,parametros);
                cursorNumeroDeYears.moveToNext();
            }
            cursorNumeroDeYears.close();
        }
        db.close();


    }

    /* ***************************  METODOS FIN  ****************************************** */

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
