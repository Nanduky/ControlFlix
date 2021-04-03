package com.nanduky.controlflix;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Inicio.OnFragmentInteractionListener,HojaMensual.OnFragmentInteractionListener,
        OtrasActividades2.OnFragmentInteractionListener, Archivo.OnFragmentInteractionListener, ArchivoConsultas.OnFragmentInteractionListener,
        ArchivoConsultasVerDias.OnFragmentInteractionListener, SeleccionMultiple.OnFragmentInteractionListener {
    //variable para recoger el año seleccionado en archivo.class
    private String stPrivateYear;
    //emparejar el fragment de otrasActividades2
    OtrasActividades2 fragmentOtrasActividades2;
    //emparejar el fragmento ArchivoConsultas
    ArchivoConsultas fragmentoArchivoConsultas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//com.nanduky.controlflix

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //para cargar la pagina de inicio cuando se inicie la app
        Fragment fragment = new Inicio();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main,fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //para cargar el fragment de otras actividades 2
        fragmentOtrasActividades2 = new OtrasActividades2();
        //metodo para llamar a otras actividades
        otrasActividadesVarias();
        //para cargar el fragmento a ArchivoConsultas
        fragmentoArchivoConsultas = new ArchivoConsultas();
/*        //Metodo par ir a ArchivoConsultas
        archivoConsultasYearMes();*/



    }

    

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //menu lateral donde se selecciona la actividad donde quieres ir
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // variables privadas para capturar las ids de los fragments
        int id = item.getItemId();

        Fragment miFragment = null;
        boolean fragmentSeleccionado =false;

        if (id == R.id.nav_inicio) {
            // Handle the Inicio
            miFragment = new Inicio();
             fragmentSeleccionado = true;

        } else if (id == R.id.nav_hoja_mensual) {
            //para pasar a hoja mensual
            miFragment = new HojaMensual();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_archivo) {
            //Para el archivo que guarda los meses anteriores
            miFragment = new Archivo();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        if(fragmentSeleccionado ==  true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    //El metodo para llamar a otrasActividades
    public void otrasActividadesVarias() {
        //recoger el dato del paquete que viene de inicio.java cuando se pulsa el boton otras
        // actividades
        Bundle miPaquete  = this.getIntent().getExtras();
        //se le pasa el fragment pulsado
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //comprobar que el paquete no esté vacio
        if(miPaquete != null){
            //se le pasa a la variable el dato del paquete
            int btpu = miPaquete.getInt("botonOtrasActividades");
            //si el bulto coincide con la comparación
            if (btpu == R.id.btOotras) {
                //ir al la actividad OtrasActividades.java
                transaction.replace(R.id.content_main,fragmentOtrasActividades2);
            }
        }

        //se inicia el fragment
        transaction.commit();

    }

/*    //Metodo para ir a archivoConsultas
    private void archivoConsultasYearMes() {
        //recoger el dato del paqueteYear que viene de Archivo,java cuando se pulsa el año elegido
        Bundle miPaqueteYears = this.getIntent().getExtras();
        //se le pasa el fragment pulsado
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //comprobar que el paqueteYear noesté vacio
        if(miPaqueteYears != null){
            //pasar el dato del paquete a la variable
            stPrivateYear = miPaqueteYears.getString("miYearSeleccionado");
            //ir al la actividad ArchivoConsultas
            transaction.replace(R.id.content_main,fragmentoArchivoConsultas);
        }
        //se inicia el fragment
        transaction.commit();
    }*/
}
