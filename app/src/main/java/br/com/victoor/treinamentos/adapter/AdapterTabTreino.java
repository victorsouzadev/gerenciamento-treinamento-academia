package br.com.victoor.treinamentos.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import br.com.victoor.treinamentos.TreinoFragment;
import br.com.victoor.treinamentos.model.Treino;
import br.com.victoor.treinamentos.session.DataUser;
import br.com.victoor.treinamentos.utils.Base64Custom;


public class AdapterTabTreino extends FragmentPagerAdapter {
    private List<Treino> tabs;
    private Context context;
    public AdapterTabTreino(@NonNull FragmentManager fm, Context context, List<Treino> treinos) {
        super(fm);
        this.context = context;
        this.tabs = treinos;
    }
    //private Chamado chamado;
    //private Usuario usuario;



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        fragment = new TreinoFragment(context, "user/"+Base64Custom.codificaBase64(DataUser.getPersonEmail())+"/treinos/"+tabs.get(position).getId());
//        switch (position){
//            case 0:{
//                fragment = new TreinoFragment();
//                break;
//            }
//            case 1: {
//                fragment = new TreinoFragment();
//                break;
//            }
//        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getName();
    }

    public void atualizar(Treino treino){
        tabs.set(tabs.size(),treino);
        this.notifyDataSetChanged();
    }



}
