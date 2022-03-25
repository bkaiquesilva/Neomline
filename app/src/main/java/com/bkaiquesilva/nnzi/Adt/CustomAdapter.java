package com.bkaiquesilva.nnzi.Adt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bkaiquesilva.nnzi.ChatActivity;
import com.bkaiquesilva.nnzi.Pendentesss;
import com.bkaiquesilva.nnzi.PerfilContato;
import com.bkaiquesilva.nnzi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<Blog> dogies;
    LayoutInflater inflater;

    public CustomAdapter(Context c, ArrayList<Blog> dogies) {
        this.c = c;
        this.dogies = dogies;
    }

    public void updateList(List<Blog> newList) {
        dogies = new ArrayList<>();
        dogies.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dogies.size();
    }

    @Override
    public Object getItem(int i) {
        return dogies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertview, ViewGroup viewGroup) {

        if (inflater == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertview == null) {
            convertview = inflater.inflate(R.layout.item_contatos, viewGroup, false);

        }

        final MyHolder holder = new MyHolder(convertview);

        PicassoClient.downloadimg2(c, dogies.get(i).getIMAGE(), holder.perssone, holder.fake_iv_card_profile);

        if (dogies.get(i).getIdentificador().equals("wew") || dogies.get(i).getMeu_identifica().equals("84967495GRUPO")) {
                holder.mView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
                holder.mView.setVisibility(View.GONE);
            }


            String trimed = dogies.get(i).getEnome().trim();
            int palavras = trimed.isEmpty() ? 0 : trimed.split("\\s+").length;

            if (palavras == 1) {
                holder.off_on.setText(dogies.get(i).getEnome());
                holder.user_name.setText(dogies.get(i).getIdentificador());
            } else {
                StringTokenizer tokenizer = new StringTokenizer(dogies.get(i).getEnome(), " ");
                String primeiro = tokenizer.nextToken();
                String segundo = tokenizer.nextToken();
                holder.off_on.setText(primeiro + " " + segundo);
                holder.user_name.setText(dogies.get(i).getIdentificador());
            }

            if (!dogies.get(i).getNova_msn().equals("7656")) {
                if (dogies.get(i).getNova_msn().equals("0")) {
                    holder.nova_mensagem.setVisibility(View.GONE);

                } else if (dogies.get(i).getNova_msn().equals("1")) {
                    holder.nova_mensagem.setVisibility(View.VISIBLE);
                    String next = "<font color='#43C648'>NOVA</font>";
                    String trimedi = dogies.get(i).getEnome().trim();
                    int palavrasi = trimedi.isEmpty() ? 0 : trimedi.split("\\s+").length;

                    if (palavrasi == 1) {
                        holder.off_on.setText(Html.fromHtml(dogies.get(i).getEnome() + " " + next));
                    } else {
                        StringTokenizer tokenizer = new StringTokenizer(dogies.get(i).getEnome(), " ");
                        String primeiro = tokenizer.nextToken();
                        String segundo = tokenizer.nextToken();
                        holder.off_on.setText(Html.fromHtml(primeiro  + " " + segundo + " " + next));
                    }
                }
            } else {
                holder.nova_mensagem.setVisibility(View.VISIBLE);
                String next = "<font color='#43C648'>PENDENTE</font>";
                String trimede = dogies.get(i).getEnome().trim();
                int palavrase = trimede.isEmpty() ? 0 : trimede.split("\\s+").length;

                if (palavrase == 1) {
                    holder.off_on.setText(Html.fromHtml(dogies.get(i).getEnome() + " " + next));
                } else {
                    StringTokenizer tokenizer = new StringTokenizer(dogies.get(i).getEnome(), " ");
                    String primeiro = tokenizer.nextToken();
                    String segundo = tokenizer.nextToken();
                    holder.off_on.setText(Html.fromHtml(primeiro  + " " + segundo + " " + next));
                }
            }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dogies.get(i).getNova_msn().equals("7656")) {
                        if (!dogies.get(i).getMeu_identifica().equals("84967495GRUPO")) {
                            Activity activity = (Activity) c;
                            Intent loginIntent = new Intent(c, ChatActivity.class);
                            loginIntent.putExtra("y_nome", dogies.get(i).getEnome());
                            loginIntent.putExtra("y_image", dogies.get(i).getIMAGE());
                            loginIntent.putExtra("y_uide", dogies.get(i).getUid());
                            loginIntent.putExtra("y_keys", dogies.get(i).getKeys());
                            loginIntent.putExtra("y_identifica", dogies.get(i).getIdentificador());
                            loginIntent.putExtra("y_meu_identifica", dogies.get(i).getMeu_identifica());
                            c.startActivity(loginIntent);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            activity.finish();
                        }
                } else {
                    Activity activity = (Activity) c;
                    Intent loginIntent = new Intent(c, Pendentesss.class);
                    loginIntent.putExtra("y_nome", dogies.get(i).getEnome());
                    loginIntent.putExtra("y_image", dogies.get(i).getIMAGE());
                    loginIntent.putExtra("y_keys", dogies.get(i).getKeys());
                    loginIntent.putExtra("y_uide", dogies.get(i).getUid());
                    c.startActivity(loginIntent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    activity.finish();
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!dogies.get(i).getNova_msn().equals("7656")) {
                    if (!dogies.get(i).getMeu_identifica().equals("84967495GRUPO")) {
                        Activity activity = (Activity) c;
                        Intent loginIntent = new Intent(c, PerfilContato.class);
                        loginIntent.putExtra("euide", dogies.get(i).getUid());
                        loginIntent.putExtra("y_image", dogies.get(i).getIMAGE());
                        loginIntent.putExtra("y_keys", dogies.get(i).getKeys());
                        c.startActivity(loginIntent);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        activity.finish();
                    }
            }
                return false;
            }
        });

        holder.perssone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity)c;
                ImageView img_bu;
                            final Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.auver);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            img_bu = dialog.findViewById(R.id.btimg);


                            img_bu.setBackgroundResource(0);
                            Picasso.get().load(dogies.get(i).getIMAGE()).into(img_bu);

                            img_bu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                        dialog.setCancelable(true);
                        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
                        dialog.show();
            }
        });
        return convertview;
    }
}
