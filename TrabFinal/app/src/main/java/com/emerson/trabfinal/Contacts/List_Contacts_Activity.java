package com.emerson.trabfinal.Contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.emerson.trabfinal.Objects.Contact;
import com.emerson.trabfinal.R;
import com.emerson.trabfinal.ViewHolder.ViewHolderContact;

public class List_Contacts_Activity extends AppCompatActivity {

    RecyclerView RVContacts;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference DB_Users;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Contact, ViewHolderContact> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contact> firebaseRecyclerOptions;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Contatos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        RVContacts = findViewById(R.id.RVContacts);
        RVContacts.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DB_Users = firebaseDatabase.getReference("Users");

        dialog = new Dialog(List_Contacts_Activity.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        ListContacts();
    }

    private void ListContacts() {
        Query query = DB_Users.child(user.getUid()).child("Contacts").orderByChild("Name");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query, Contact.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contact, ViewHolderContact>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContact viewHolderContact, int position, @NonNull Contact contact) {
                viewHolderContact.SetDataContact(
                        getApplicationContext(),
                        contact.getId_contact(),
                        contact.getUid_contact(),
                        contact.getName(),
                        contact.getLastname(),
                        contact.getMail(),
                        contact.getPhone(),
                        contact.getAge(),
                        contact.getHome(),
                        contact.getImage()
                );
            }

            @NonNull
            @Override
            public ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
                ViewHolderContact viewHolderContact = new ViewHolderContact(view);
                viewHolderContact.setOnClickListener(new ViewHolderContact.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       // Toast.makeText(List_Contacts_Activity.this, "On item click", Toast.LENGTH_SHORT).show();
                        //GETTING SELECTED USER DATA
                        String id_c = getItem(position).getId_contact();
                        String uid_user = getItem(position).getUid_contact();
                        String name_c = getItem(position).getName();
                        String lastname_c = getItem(position).getLastname();
                        String mail_c = getItem(position).getMail();
                        String phone_c = getItem(position).getPhone();
                        String age_c = getItem(position).getAge();
                        String home_c = getItem(position).getHome();
                        String image_c = getItem(position).getImage();

                        Intent intent = new Intent(List_Contacts_Activity.this, Detail_Contact_Activity.class);
                        intent.putExtra("id_c", id_c);
                        intent.putExtra("uid_user", uid_user);
                        intent.putExtra("name_c", name_c);
                        intent.putExtra("lastname_c", lastname_c);
                        intent.putExtra("mail_c", mail_c);
                        intent.putExtra("phone_c", phone_c);
                        intent.putExtra("age_c", age_c);
                        intent.putExtra("home_c", home_c);
                        intent.putExtra("image_c", image_c);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_c = getItem(position).getId_contact();
                        String uid_user = getItem(position).getUid_contact();
                        String name_c = getItem(position).getName();
                        String lastname_c = getItem(position).getLastname();
                        String mail_c = getItem(position).getMail();
                        String phone_c = getItem(position).getPhone();
                        String age_c = getItem(position).getAge();
                        String home_c = getItem(position).getHome();
                        String image_c = getItem(position).getImage();

                        //String id_contact = getItem(position).getId_contact();
                        Button Btn_delete_Contact, Btn_Update_Contact;

                        dialog.setContentView(R.layout.dialog_box_options_contact);

                        Btn_delete_Contact = dialog.findViewById(R.id.Btn_delete_Contact);
                        Btn_Update_Contact = dialog.findViewById(R.id.Btn_Update_Contact);
                        
                        Btn_delete_Contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(List_Contacts_Activity.this, "Delete contact", Toast.LENGTH_SHORT).show();
                                DeleteContact(id_c);
                                dialog.dismiss();
                            }
                        });

                        Btn_Update_Contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(List_Contacts_Activity.this, Update_Contact_Activity.class);
                                intent.putExtra("id_c", id_c);
                                intent.putExtra("uid_user", uid_user);
                                intent.putExtra("name_c", name_c);
                                intent.putExtra("lastname_c", lastname_c);
                                intent.putExtra("mail_c", mail_c);
                                intent.putExtra("phone_c", phone_c);
                                intent.putExtra("age_c", age_c);
                                intent.putExtra("home_c", home_c);
                                intent.putExtra("image_c", image_c);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });
                return viewHolderContact;
            }
        };

        RVContacts.setLayoutManager(new GridLayoutManager(List_Contacts_Activity.this, 2 ));
        firebaseRecyclerAdapter.startListening();
        RVContacts.setAdapter(firebaseRecyclerAdapter);
    }

    private void SearchContacts(String Name_Contact) {
        Query query = DB_Users.child(user.getUid()).child("Contacts").orderByChild("name").startAt(Name_Contact).endAt(Name_Contact + "\uf8ff");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contact>().setQuery(query, Contact.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contact, ViewHolderContact>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContact viewHolderContact, int position, @NonNull Contact contact) {
                viewHolderContact.SetDataContact(
                        getApplicationContext(),
                        contact.getId_contact(),
                        contact.getUid_contact(),
                        contact.getName(),
                        contact.getLastname(),
                        contact.getMail(),
                        contact.getPhone(),
                        contact.getAge(),
                        contact.getHome(),
                        contact.getImage()
                );
            }

            @NonNull
            @Override
            public ViewHolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
                ViewHolderContact viewHolderContact = new ViewHolderContact(view);
                viewHolderContact.setOnClickListener(new ViewHolderContact.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(List_Contacts_Activity.this, "On item click", Toast.LENGTH_SHORT).show();
                        //GETTING SELECTED USER DATA
                        String id_c = getItem(position).getId_contact();
                        String uid_user = getItem(position).getUid_contact();
                        String name_c = getItem(position).getName();
                        String lastname_c = getItem(position).getLastname();
                        String mail_c = getItem(position).getMail();
                        String phone_c = getItem(position).getPhone();
                        String age_c = getItem(position).getAge();
                        String home_c = getItem(position).getHome();
                        String image_c = getItem(position).getImage();

                        Intent intent = new Intent(List_Contacts_Activity.this, Detail_Contact_Activity.class);
                        intent.putExtra("id_c", id_c);
                        intent.putExtra("uid_user", uid_user);
                        intent.putExtra("name_c", name_c);
                        intent.putExtra("lastname_c", lastname_c);
                        intent.putExtra("mail_c", mail_c);
                        intent.putExtra("phone_c", phone_c);
                        intent.putExtra("age_c", age_c);
                        intent.putExtra("home_c", home_c);
                        intent.putExtra("image_c", image_c);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_c = getItem(position).getId_contact();
                        String uid_user = getItem(position).getUid_contact();
                        String name_c = getItem(position).getName();
                        String lastname_c = getItem(position).getLastname();
                        String mail_c = getItem(position).getMail();
                        String phone_c = getItem(position).getPhone();
                        String age_c = getItem(position).getAge();
                        String home_c = getItem(position).getHome();
                        String image_c = getItem(position).getImage();

                        //String id_contact = getItem(position).getId_contact();
                        Button Btn_delete_Contact, Btn_Update_Contact;

                        dialog.setContentView(R.layout.dialog_box_options_contact);

                        Btn_delete_Contact = dialog.findViewById(R.id.Btn_delete_Contact);
                        Btn_Update_Contact = dialog.findViewById(R.id.Btn_Update_Contact);

                        Btn_delete_Contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(List_Contacts_Activity.this, "Delete contact", Toast.LENGTH_SHORT).show();
                                DeleteContact(id_c);
                                dialog.dismiss();
                            }
                        });

                        Btn_Update_Contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(List_Contacts_Activity.this, Update_Contact_Activity.class);
                                intent.putExtra("id_c", id_c);
                                intent.putExtra("uid_user", uid_user);
                                intent.putExtra("name_c", name_c);
                                intent.putExtra("lastname_c", lastname_c);
                                intent.putExtra("mail_c", mail_c);
                                intent.putExtra("phone_c", phone_c);
                                intent.putExtra("age_c", age_c);
                                intent.putExtra("home_c", home_c);
                                intent.putExtra("image_c", image_c);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });
                return viewHolderContact;
            }
        };

        RVContacts.setLayoutManager(new GridLayoutManager(List_Contacts_Activity.this, 2 ));
        firebaseRecyclerAdapter.startListening();
        RVContacts.setAdapter(firebaseRecyclerAdapter);
    }

    private void DeleteContact(String idContact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(List_Contacts_Activity.this);
        builder.setTitle("Excluir");
        builder.setMessage("Deseja excluir este contato?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = DB_Users.child(user.getUid()).child("Contacts").orderByChild("id_contact").equalTo(idContact);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(List_Contacts_Activity.this, "Contato Excluido", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(List_Contacts_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(List_Contacts_Activity.this, "Cancelado pelo usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void Empty_Register_contact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(List_Contacts_Activity.this);
        builder.setTitle("Excuir todos os contatos");
        builder.setMessage("Tem certeza de que deseja excluir todos os contatos?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = DB_Users.child(user.getUid()).child("Contacts");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(List_Contacts_Activity.this, "Todos os contatos foram excluídos", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(List_Contacts_Activity.this, "Cancelado pelo usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_contact, menu);
        MenuItem item = menu.findItem(R.id.Search_Contact);
        SearchView searchView = (SearchView) item.getActionView();
        assert searchView != null;
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchContacts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchContacts(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Add_contact) {
            String Uid_recovered = getIntent().getStringExtra("Uid");
            Intent intent = new Intent(List_Contacts_Activity.this, Add_Contact_Activity.class);
            intent.putExtra("Uid", Uid_recovered);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.EmptyContacts){
            Empty_Register_contact();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}