import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.Scanner;

public class admin {
	private static int aux_para_nome_ambiente=1;
	private static int aux_para_nome_disp=1;
	
	
	public static ArrayList<Ambiente> lista_ambientes(JavaSpace space) {
		Ambiente template_amb=new Ambiente();
		Ambiente aux;
		int a;
		ArrayList<Ambiente> lista_objetos=new ArrayList<Ambiente>();
		Count_to_all template_count_amb=new Count_to_all();
		template_count_amb.Tipo_count="Ambiente";
		try {
			Count_to_all count_amb = (Count_to_all) space.read(template_count_amb, null, 60 * 1000);
			int i = count_amb.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Ambiente) space.take(template_amb, null, 60 * 1000);
				lista_objetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Ambiente) lista_objetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return lista_objetos;
	}
	
	public static void add_ambiente_no_espaco(JavaSpace space) {
		Ambiente amb=new Ambiente();
		Count_to_all template_count_amb=new Count_to_all();
		template_count_amb.Tipo_count="Ambiente";
		amb.Nome_Ambiente="amb"+aux_para_nome_ambiente;
		aux_para_nome_ambiente+=1;
		try {
			Count_to_all count_amb = (Count_to_all) space.take(template_count_amb, null, 60 * 1000);
			System.out.println("Quantidade de Ambiente era: "+count_amb.quantidade);
			//1 hora
			space.write(amb, null, Lease.FOREVER);
			count_amb.quantidade+=1;
			System.out.println("Quantidade de Ambiente é: "+count_amb.quantidade);
			space.write(count_amb, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	public static ArrayList<Dispositivos> lista_dispositivos(JavaSpace space) {
		Dispositivos template_disp=new Dispositivos();
		Dispositivos aux;
		int a;
		ArrayList<Dispositivos>lista_objetos=new ArrayList<Dispositivos>();
		Count_to_all template_count_disp=new Count_to_all();
		template_count_disp.Tipo_count="Dispositivo";
		try {
			Count_to_all count_disp = (Count_to_all) space.read(template_count_disp, null, 60 * 1000);
			int i = count_disp.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Dispositivos) space.take(template_disp, null, 60 * 1000);
				lista_objetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Dispositivos) lista_objetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return lista_objetos;
	}
	
	public static void add_dispositivo_no_espaco(JavaSpace space,String ambiente) {
		Dispositivos disp=new Dispositivos();
		Count_to_all template_count_disp=new Count_to_all();
		template_count_disp.Tipo_count="Dispositivo";
		disp.Ambiente_onde_se_encontra=ambiente;
		disp.Nome_Dispositivo="disp"+aux_para_nome_disp;
		aux_para_nome_disp+=1;
		try {
			Count_to_all count_disp = (Count_to_all) space.take(template_count_disp, null, 60 * 1000);
			System.out.println("Quantidade de Dispositivos eram: "+count_disp.quantidade);
			//1 hora
			space.write(disp, null, Lease.FOREVER);
			count_disp.quantidade+=1;
			System.out.println("Quantidade de Dispositivos são: "+count_disp.quantidade);
			space.write(count_disp, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	

    public static void main(String[] args) {
    	Count_to_all count_amb=new Count_to_all();
    	Count_to_all count_user=new Count_to_all();
    	Count_to_all count_disp=new Count_to_all();
    	count_amb.Tipo_count="Ambiente";
    	count_user.Tipo_count="Usuario";
    	count_disp.Tipo_count="Dispositivo";
    	count_amb.quantidade=new Integer(0);
    	count_user.quantidade=new Integer(0);
    	count_disp.quantidade=new Integer(0);
    	ArrayList lista=new ArrayList();
    	//configuração pra achar o space
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            JavaSpace space = (JavaSpace) finder.getService();
            if (space == null) {
                    System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                    System.exit(-1);
            } 
            System.out.println("O servico JavaSpace foi encontrado.");
        //configuração pra achar o space
            //Esse é o ambiente default de todos os outros, o amb1
            space.write(count_amb, null, Lease.FOREVER);
            space.write(count_user, null, Lease.FOREVER);
            space.write(count_disp, null, Lease.FOREVER);
            add_ambiente_no_espaco(space);
            
            
            lista=lista_ambientes(space);
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).Nome_Ambiente);
    		}
            add_ambiente_no_espaco(space);
            lista=lista_ambientes(space);
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).Nome_Ambiente);
    		}
            add_ambiente_no_espaco(space);
            lista=lista_ambientes(space);
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).Nome_Ambiente);
    		}
            add_ambiente_no_espaco(space);
            lista=lista_ambientes(space);
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).Nome_Ambiente);
    		}
            
            
            /*Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Entre com o texto da mensagem (ENTER para sair): ");
                String message = scanner.nextLine();
                if (message == null || message.equals("")) {
                    System.exit(0);
                }
                Message msg = new Message();
                msg.content = message;
                space.write(msg, null, 60 * 1000);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
