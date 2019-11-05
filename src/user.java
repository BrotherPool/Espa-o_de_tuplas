import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.Scanner;
public class user {
	private static String meuNome;
	private static String meuAmbiente;
	
	public static ArrayList<Ambiente> ListaAmbientes(JavaSpace space) {
		Ambiente templateAmb=new Ambiente();
		Ambiente aux;
		int a;
		ArrayList<Ambiente> listaObjetos=new ArrayList<Ambiente>();
		CountToAll templatCountAmb=new CountToAll();
		templatCountAmb.tipoCount="Ambiente";
		try {
			CountToAll count_amb = (CountToAll) space.read(templatCountAmb, null, 60 * 1000);
			int i = count_amb.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Ambiente) space.take(templateAmb, null, 60 * 1000);
				listaObjetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Ambiente) listaObjetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return listaObjetos;
	}
	
	public static ArrayList<Dispositivos> ListaDispositivos(JavaSpace space,String ambienteOndeSeEncontra) {
		Dispositivos templateDisp=new Dispositivos();
		Dispositivos aux;
		int a=0,i=0;
		ArrayList<Dispositivos>listaObjetos=new ArrayList<Dispositivos>();
		ArrayList<Dispositivos>listaRetorno=new ArrayList<Dispositivos>();
		CountToAll templatCountDisp=new CountToAll();
		templatCountDisp.tipoCount="Dispositivo";
		try {
			CountToAll countDisp = (CountToAll) space.read(templatCountDisp, null, 60 * 1000);
			i = countDisp.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Dispositivos) space.take(templateDisp, null, 60 * 1000);
				listaObjetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Dispositivos) listaObjetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		for (a=0;a<i;a++) {
			if(listaObjetos.get(a).ambienteOndeSeEncontra.equals(ambienteOndeSeEncontra)) {
				listaRetorno.add(listaObjetos.get(a));
			}
		}
		
		return listaRetorno;
	}
	
	//funcionalidades do usuario
	public static ArrayList<Usuario> ListaUsuarios(JavaSpace space,String ambienteOndeSeEncontra) {
		Usuario templateUser=new Usuario();
		Usuario aux;
		int a=0,i=0;
		ArrayList<Usuario>listaObjetos=new ArrayList<Usuario>();
		ArrayList<Usuario>listaRetorno=new ArrayList<Usuario>();
		CountToAll templatCountUser=new CountToAll();
		templatCountUser.tipoCount="Usuario";
		try {
			CountToAll countUser = (CountToAll) space.read(templatCountUser, null, 60 * 1000);
			i = countUser.quantidade.intValue();
			for (a=0;a<i;a++) {
				aux=(Usuario) space.take(templateUser, null, 60 * 1000);
				listaObjetos.add(aux);
			}
			for (a=0;a<i;a++) {
				aux=(Usuario) listaObjetos.get(a);
				space.write(aux, null, Lease.FOREVER);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		for (a=0;a<i;a++) {
			if(listaObjetos.get(a).ambienteOndeSeEncontra.equals(ambienteOndeSeEncontra)) {
				listaRetorno.add(listaObjetos.get(a));
			}
		}
		return listaRetorno;
	}
	
	
	public static void AddUsuarioNoEspaco(JavaSpace space,String ambiente) {
		Usuario user=new Usuario();
		CountToAll templateCountUser=new CountToAll();
		templateCountUser.tipoCount="Usuario";
		user.ambienteOndeSeEncontra=ambiente;
		meuAmbiente=ambiente;
		try {
			CountToAll countUser = (CountToAll) space.take(templateCountUser, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			user.nomeUsuario="user"+countUser.auxParaNome;
			meuNome=user.nomeUsuario;
			countUser.auxParaNome+=1;
			//1 hora
			space.write(user, null, Lease.FOREVER);
			countUser.quantidade+=1;
			//System.out.println("Quantidade de Usuarios são: "+countUser.quantidade);
			space.write(countUser, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}//funcionalidades do usuario
	
	//o usuario tem que estar dentro do ambiente, ou seja, antes deve ser feita uma verificação se ele se encontra dentro
	public static void RemoveUsuario(JavaSpace space,String nomeUsuario) {
		Usuario templateUser=new Usuario();
		templateUser.nomeUsuario=nomeUsuario;
		CountToAll templateCountUser=new CountToAll();
		templateCountUser.tipoCount="Usuario";
		try {
			CountToAll countUser = (CountToAll) space.take(templateCountUser, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			countUser.quantidade-=1;
			space.write(countUser, null, Lease.FOREVER);
			space.take(templateUser, null, 60 * 1000);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}//

	public static void UsuarioTrocaDeAmbiente(JavaSpace space,String nomeUsuario,String ambienteQueEleChega) {
		Usuario templateUser=new Usuario();
		templateUser.nomeUsuario=nomeUsuario;
		try {
			Usuario user = (Usuario) space.take(templateUser, null, 60 * 1000);
			user.ambienteOndeSeEncontra=ambienteQueEleChega;
			meuAmbiente=ambienteQueEleChega;
			space.write(user, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}//
	
	public static String UsuarioMandaMensagem(JavaSpace space,String usuarioQueMandaMensagem,String ambiente,String msg){
		ArrayList lista=new ArrayList();
		lista=ListaUsuarios(space,ambiente);
		int tamanho=lista.size(),i;
		Mensagem mensagemASerMandada=new Mensagem();
		mensagemASerMandada.mensagem=msg;
		mensagemASerMandada.quemEnviou=usuarioQueMandaMensagem;
		mensagemASerMandada.ambienteOndeSeEncontra=ambiente;
		//mensagemASerMandada.paraQuemEnviou=usuarioQueRecebeMensagem;
		try {
			for (i=0;i<tamanho;i++) {
				mensagemASerMandada.paraQuemEnviou=((Usuario)lista.get(i)).nomeUsuario;
				if(mensagemASerMandada.paraQuemEnviou.equals(usuarioQueMandaMensagem)) {
					space.write(mensagemASerMandada, null, Lease.FOREVER);
				}
				
			}
			return usuarioQueMandaMensagem+"): "+msg;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "Não foi possível enviar mensagem, tente novamente";
	}//
	
	public static String UsuarioRecebeMensagem(JavaSpace space,String usuarioQueRecebeMensagem,String ambiente){
		Mensagem templateMensagem=new Mensagem();
		templateMensagem.paraQuemEnviou=usuarioQueRecebeMensagem;
		templateMensagem.ambienteOndeSeEncontra=ambiente;
		try {
			Mensagem mensagem = (Mensagem) space.take(templateMensagem, null, 60 * 1000);
			if (!(mensagem==null)) {
				return mensagem.quemEnviou+"): "+mensagem.mensagem;
			}
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "";
	}//
	
	private static void clearConsole()
	{
	   try
	   {
	       String os = System.getProperty("os.name");

	       if (os.contains("Windows"))
	       {
	           Runtime.getRuntime().exec("cls");
	       }
	       else
	       {
	           Runtime.getRuntime().exec("clear");
	       }
	   }
	   catch (Exception exception)
	   {
	       //  Handle exception.
	   }
	}
	
	public static void PrintListaAmbientes(JavaSpace space) {
		int i;
		ArrayList lista=new ArrayList();
		lista=ListaAmbientes(space);
		System.out.println("\n**********Listagem de Ambientes***********");
        for (i = 0; i < lista.size(); i++) {
			System.out.println("Ambiente:            ->               "+((Ambiente)lista.get(i)).nomeAmbiente);
		}
        System.out.println("******************************************\n");
	}
	
	public static void PrintListaDispositivos(JavaSpace space) {
		int i,j;
		ArrayList listaDisp=new ArrayList();
		ArrayList listaAmb=new ArrayList();
		listaAmb=ListaAmbientes(space);
		System.out.println("\n*********Listagem de Dispositivos*********");
		for (j=0;j<listaAmb.size();j++) {
			listaDisp=ListaDispositivos(space,((Ambiente)listaAmb.get(j)).nomeAmbiente);
			for (i = 0; i < listaDisp.size(); i++) {
	        	System.out.println("Dispositivo:       -> "+((Dispositivos)listaDisp.get(i)).nomeDispositivo+" dentro do "+((Ambiente)listaAmb.get(j)).nomeAmbiente);
			}
		}		
        System.out.println("******************************************\n");
	}
	
	public static void PrintListaUser(JavaSpace space) {
		int i,j;
		ArrayList listaUser=new ArrayList();
		ArrayList listaAmb=new ArrayList();
		listaAmb=ListaAmbientes(space);
		System.out.println("\n***********Listagem de Usuários***********");
		for (j=0;j<listaAmb.size();j++) {
			listaUser=ListaUsuarios(space,((Ambiente)listaAmb.get(j)).nomeAmbiente);
			for (i = 0; i < listaUser.size(); i++) {
	        	System.out.println("Usuário:       ->     "+((Usuario)listaUser.get(i)).nomeUsuario+" dentro do "+((Ambiente)listaAmb.get(j)).nomeAmbiente);
			}
		}		
        System.out.println("******************************************\n");
	}	
	
	public static void UserInterface(JavaSpace space) {
		PrintListaAmbientes(space);
		ArrayList lista=new ArrayList();
		Scanner scanner = new Scanner(System.in);
		lista=ListaAmbientes(space);
		System.out.print("Qual ambiente o usuário será inserido? ");
        String ambiente = scanner.nextLine();
        int i;
        for (i = 0; i < lista.size(); i++) {
        	if((ambiente.equals(((Ambiente)lista.get(i)).nomeAmbiente))) {
        		AddUsuarioNoEspaco(space,ambiente);
        		ThreadRecebe(space);
        		Chat(space);
        		break;
        	}
		}
        if(i==lista.size()) {
        	System.out.println("\n*********Ambiente não encontrado**********\n");
        }
        //Menu(space);
	}
	
	public static void Chat(JavaSpace space) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("\n"+meuNome+"): ");
        String mensagem = scanner.nextLine();
		UsuarioMandaMensagem(space,meuNome,meuAmbiente,mensagem);
		Chat(space);
		
	}
	
	public static void ThreadRecebe(JavaSpace space) {
		Thread t1 = new Thread(new Runnable() {
		    public void run()
		    {
		    	while(true) {
		    		System.out.println("Mensagem que recebi: "+UsuarioRecebeMensagem(space, meuNome, meuAmbiente));
		    	}
		    	
		    }});  
		    t1.start();
	}
	
	//criar comando /muda para mudar o ambiente, e arrumar a thread
	
	public static void main(String[] args) {
		try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            JavaSpace space = (JavaSpace) finder.getService();
            if (space == null) {
                    System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                    System.exit(-1);
            } 
            System.out.println("O servico JavaSpace foi encontrado.");
            UserInterface(space);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}
