package br.com.victoor.treinamentos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.victoor.treinamentos.model.Exercicio;
import br.com.victoor.treinamentos.model.Membro;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.model.User;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;

public class MenuUserGerenciamento extends AppCompatActivity implements View.OnClickListener{

    private static final int STORAGE_CODE = 1000;
    private User user;
    private FirebaseDatabase database;
    private Button bntGeneratePDF;
    private Button bntTreinos;
    private Button bntRemoveUser;
    private Button bntEditUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user_gerenciamento);

        bntTreinos = findViewById(R.id.bnt_treinos);
        bntTreinos.setOnClickListener(this);

        bntRemoveUser = findViewById(R.id.bnt_remove_user);
        bntRemoveUser.setOnClickListener(this);

        bntEditUser = findViewById(R.id.bnt_edit_user);
        bntEditUser.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        configToolbar();
        configGeneratePDF();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataPdf();
    }

    private void configToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(this.user.getName());
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuUserGerenciamento.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.write), PorterDuff.Mode.SRC_ATOP);




    }

    private void configGeneratePDF() {
        bntGeneratePDF = findViewById(R.id.bnt_generate_pdf);
        bntGeneratePDF.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnt_treinos:{
                Intent intent = new Intent(this, TreinoCadastro.class);
                intent.putExtra("user", user);
                startActivity(intent);
                break;
            }  case R.id.bnt_generate_pdf:{
                Toast.makeText(this, "Generate pdf", Toast.LENGTH_SHORT).show();
                PermissionsStorage();
                break;
            } case R.id.bnt_remove_user:{
                removeUser();
                break;
            } case R.id.bnt_edit_user:{
                Intent intent = new Intent(this, CadastroUser.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    private void removeUser() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //View mView = getLayoutInflater().inflate(R.layout.dialog_alert_message,null);
       // TextView textMessage = mView.findViewById(R.id.alert_message);
        builder.setTitle("Remover "+user.getName()+"?");
        //textMessage.setText("Remover "+user.getName()+"?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String path = "user/" + Base64Custom.codificaBase64(user.getEmail());
                database.getReference(path).removeValue();
                finish();

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(GerenciarUsuarios.this, "cancelou", Toast.LENGTH_SHORT).show();
            }
        });





        //builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void PermissionsStorage() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_CODE);
            }else{
                savePdf();

            }
        }else{
            savePdf();

        }
    }

    private void savePdf() {
        Document mDoc = new Document(PageSize.A4.rotate());
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String filePath = Environment.getExternalStorageDirectory()+"/ficha_treino_alunos/"+user.getName()+fileName+".pdf";

        File folder = new File(Environment.getExternalStorageDirectory()+"/ficha_treino_alunos");

        if(!folder.exists()){
            folder.mkdir();
        }


      /*  PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo detalhesDaPagina = new PdfDocument.PageInfo.Builder(500,600,1).create();

        PdfDocument.Page novaPagina = document.startPage(detalhesDaPagina);

        Canvas canvas = novaPagina.getCanvas();

        Paint corDoTexto = new Paint();
        corDoTexto.setColor(Color.MAGENTA);
        corDoTexto.setStrokeWidth(2f);
        String texto = "texto texto texto texto texto \n texto texto texto texto \n texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto texto";

//        new String(texto);
//        List<char[]> textoList= Arrays.asList(texto.toCharArray());
//        new String();
//        canvas.
        canvas.drawText(texto, 10,  20,corDoTexto);
        canvas.drawLine(20,20,20,200,corDoTexto);
        canvas.drawLine(20,200,200,200,corDoTexto);
        //canvas.drawLine(20,30,100,200,corDoTexto);
        //canvas.drawLine(20,30,100,200,corDoTexto);
        document.finishPage(novaPagina);

        try{
            File filePathFile = new File(filePath);
            document.writeTo(new FileOutputStream(filePathFile));
        }catch (Exception e){

        }
*/

        try {
            PdfWriter.getInstance(mDoc,new FileOutputStream(filePath));
            mDoc.addAuthor("Victor Ferreira Souza");
            mDoc.open();

            int columns = DataUser.getUser().getTreinos().size();
            List<Treino> treinosPdf = DataUser.getUser().getTreinos();

            PdfPTable table = new PdfPTable(columns);

            for (Treino treino: treinosPdf) {
                PdfPCell header = new PdfPCell(new Phrase(18, new Chunk(treino.getName(), FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))));
                table.addCell(header);
            }
            for (Treino treino: treinosPdf){
                PdfPCell cell = new PdfPCell();
                for (Membro membro:treino.getMembros()) {
                    cell.addElement(new Phrase(18, new Chunk(membro.getName(), FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD))));
                    for (Exercicio exercicio: membro.getExercicios()) {
                        String texto = exercicio.getName();
                        texto += exercicio.getRepeticoes()>0 ? "\n("+exercicio.getRepeticoes()+" Séries)": "\n(Série unica)";

                        cell.addElement(new Phrase(14, new Chunk(texto, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL))));
                    }
                }
                table.addCell(cell);
            }


//            header.setColspan(6);
//            header.setBackgroundColor(BaseColor.YELLOW);
//            header.setBorderWidthBottom(2.0f);
//            header.setBorderColorBottom(BaseColor.BLUE);
//            header.setBorder(Rectangle.BOTTOM);
//            Font regular = new Font(Font.FontFamily.HELVETICA, 18);
//            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
//            bold.setColor(BaseColor.MAGENTA);
//
//            Paragraph p1 = new Paragraph("Textpdsdsl",bold);
//
//            String texto = " Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2 Teste2";
//           // table.addCell(header);
//            PdfPCell cell = new PdfPCell();
//            cell.setBorder(NO_BORDER_TOP_BOTTON);
//            cell.setHorizontalAlignment(1);
//            cell.setVerticalAlignment(1);
//            Phrase phrase3 = new Phrase("this is a phrase with a red, normal font Courier, size 12", FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL));
//            Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));
//            Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD)));
//

//            cell.addElement(phrase3);
//            cell.addElement(phrase5);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);
//            table.addCell(cell);


            // table.setWidthPercentage(60.0f);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);


            mDoc.add(table);


            mDoc.close();
            Toast.makeText(this, "Arquivo salvo\n"+filePath, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getDataPdf() {
        setTreinoPDF();

    }

    private void setTreinoPDF() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user/"+ Base64Custom.codificaBase64(user.getEmail())+"/treinos");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                List<Treino> treinosPdf = new ArrayList<>();

                for (DataSnapshot value: dataSnapshot.getChildren()){
                    Treino treino = value.getValue(Treino.class);
                    treino.setId(value.getKey());

                    for (DataSnapshot valueMembros: value.child("/membros").getChildren()) {
                        Membro membro = valueMembros.getValue(Membro.class);


                        for (DataSnapshot valueExercicio: valueMembros.child("/exercicio").getChildren()) {
                            Exercicio exercicio = valueExercicio.getValue(Exercicio.class);
                            membro.getExercicios().add(exercicio);
                        }
                        treino.getMembros().add(membro);

                    }


                    treinosPdf.add(treino);
                }
                DataUser.getUser().setTreinos(treinosPdf);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if(grantResults.length>1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savePdf();
                }else{
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
