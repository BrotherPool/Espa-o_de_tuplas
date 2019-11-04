import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.util.ArrayList;

import java.rmi.RemoteException;
import java.util.Scanner;

public class admin {	
	
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
	
	public static void AddAmbienteNoEspaco(JavaSpace space) {
		Ambiente amb=new Ambiente();
		CountToAll templateCountAmb=new CountToAll();
		templateCountAmb.tipoCount="Ambiente";
		try {
			CountToAll countAmb = (CountToAll) space.take(templateCountAmb, null, 60 * 1000);
			//System.out.println("Quantidade de Ambiente era: "+countAmb.quantidade);
			amb.nomeAmbiente="amb"+countAmb.auxParaNome;
			countAmb.auxParaNome+=1;
			//1 hora
			space.write(amb, null, Lease.FOREVER);
			countAmb.quantidade+=1;
			//System.out.println("Quantidade de Ambiente é: "+countAmb.quantidade);
			space.write(countAmb, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
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
	
	public static void AddDispositivoNoEspaco(JavaSpace space,String ambiente) {
		Dispositivos disp=new Dispositivos();
		CountToAll templateCountDisp=new CountToAll();
		templateCountDisp.tipoCount="Dispositivo";
		disp.ambienteOndeSeEncontra=ambiente;
		try {
			CountToAll countDisp = (CountToAll) space.take(templateCountDisp, null, 60 * 1000);
			//System.out.println("Quantidade de Dispositivos eram: "+countDisp.quantidade);
			disp.nomeDispositivo="disp"+countDisp.auxParaNome;
			countDisp.auxParaNome+=1;
			//1 hora
			space.write(disp, null, Lease.FOREVER);
			countDisp.quantidade+=1;
			//System.out.println("Quantidade de Dispositivos são: "+countDisp.quantidade);
			space.write(countDisp, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
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
		try {
			CountToAll countUser = (CountToAll) space.take(templateCountUser, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			user.nomeUsuario="user"+countUser.auxParaNome;
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
	
	public static boolean PodeRemoverAmbiente(JavaSpace space,String ambiente) {
		if((ListaDispositivos(space,ambiente).size()==0) && (ListaUsuarios(space,ambiente).size()==0)) {
			System.out.println("O ambiente "+ambiente+" pode ser removido");
			return true;
        }
		System.out.println("O ambiente "+ambiente+" não pode ser removido");
		return false;
	}
	
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
	
	//o dispositivo tem que estar dentro do ambiente, ou seja, antes deve ser feita uma verificação se ele se encontra dentro
	public static void RemoveDispositivo(JavaSpace space,String nomeDispositivo) {
		Dispositivos templateDispositivo=new Dispositivos();
		templateDispositivo.nomeDispositivo=nomeDispositivo;
		CountToAll templateCountDisp=new CountToAll();
		templateCountDisp.tipoCount="Dispositivo";
		try {
			CountToAll countDisp = (CountToAll) space.take(templateCountDisp, null, 60 * 1000);
			//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
			countDisp.quantidade-=1;
			space.write(countDisp, null, Lease.FOREVER);
			space.take(templateDispositivo, null, 60 * 1000);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void RemoveAmbiente(JavaSpace space,String nomeAmbiente) {
		if(PodeRemoverAmbiente(space,nomeAmbiente)) {
			Ambiente templateAmb=new Ambiente();
			CountToAll templateCountAmb=new CountToAll();
			templateAmb.nomeAmbiente=nomeAmbiente;
			templateCountAmb.tipoCount="Ambiente";
			try {
				CountToAll countAmb = (CountToAll) space.take(templateCountAmb, null, 60 * 1000);
				//System.out.println("Quantidade de Usuarios eram: "+countUser.quantidade);
				countAmb.quantidade-=1;
				space.write(countAmb, null, Lease.FOREVER);
				space.take(templateAmb, null, 60 * 1000);
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public static void UsuarioTrocaDeAmbiente(JavaSpace space,String nomeUsuario,String ambienteQueEleChega) {
		Usuario templateUser=new Usuario();
		templateUser.nomeUsuario=nomeUsuario;
		try {
			Usuario user = (Usuario) space.take(templateUser, null, 60 * 1000);
			user.ambienteOndeSeEncontra=ambienteQueEleChega;
			space.write(user, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}//

	public static void DispositivoTrocaDeAmbiente(JavaSpace space,String nomeDispositivo,String ambienteQueEleChega) {
		Dispositivos templateDispositivo=new Dispositivos();
		templateDispositivo.nomeDispositivo=nomeDispositivo;
		try {
			Dispositivos disp = (Dispositivos) space.take(templateDispositivo, null, 60 * 1000);
			disp.ambienteOndeSeEncontra=ambienteQueEleChega;
			space.write(disp, null, Lease.FOREVER);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static String UsuarioMandaMensagem(JavaSpace space,String usuarioQueMandaMensagem,String usuarioQueRecebeMensagem,String msg){
		Mensagem mensagemASerMandada=new Mensagem();
		mensagemASerMandada.mensagem=msg;
		mensagemASerMandada.quemEnviou=usuarioQueMandaMensagem;
		mensagemASerMandada.paraQuemEnviou=usuarioQueRecebeMensagem;
		try {
			space.write(mensagemASerMandada, null, Lease.FOREVER);
			return usuarioQueMandaMensagem+"): "+msg;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "Não foi possível enviar mensagem, tente novamente";
	}//
	
	public static String UsuarioRecebeMensagem(JavaSpace space,String usuarioQueRecebeMensagem){
		Mensagem templateMensagem=new Mensagem();
		templateMensagem.paraQuemEnviou=usuarioQueRecebeMensagem;
		try {
			Mensagem mensagem = (Mensagem) space.take(templateMensagem, null, 60 * 1000);
			return mensagem.quemEnviou+"): "+mensagem.mensagem;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return "";
	}//
	
	
    
	public static void main(String[] args) {
    	CountToAll countAmb=new CountToAll();
    	CountToAll countUser=new CountToAll();
    	CountToAll countDisp=new CountToAll();
    	countAmb.tipoCount="Ambiente";
    	countUser.tipoCount="Usuario";
    	countDisp.tipoCount="Dispositivo";
    	countAmb.quantidade=new Integer(0);
    	countUser.quantidade=new Integer(0);
    	countDisp.quantidade=new Integer(0);
    	countAmb.auxParaNome=new Integer(1);
    	countUser.auxParaNome=new Integer(1);
    	countDisp.auxParaNome=new Integer(1);
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
            //contador de ambientes, usuários e dispositivos
            space.write(countAmb, null, Lease.FOREVER);
            space.write(countUser, null, Lease.FOREVER);
            space.write(countDisp, null, Lease.FOREVER);
            //Esse é o ambiente default de todos os outros, o amb1
            AddAmbienteNoEspaco(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            //teste ambientes
            lista=ListaAmbientes(space);
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
    		}
            AddAmbienteNoEspaco(space);
            lista=ListaAmbientes(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
    		}
            AddAmbienteNoEspaco(space);
            lista=ListaAmbientes(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
    		}
            AddAmbienteNoEspaco(space);
            lista=ListaAmbientes(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
    		}
            //teste de remoçao de ambiente que pode ser removido
            RemoveAmbiente(space,"amb4");
            lista=ListaAmbientes(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
    		} 
            
            //teste dispositivos
            System.out.println("\nPassou de linha\n");
            AddDispositivoNoEspaco(space,"amb1");
            lista=ListaDispositivos(space,"amb1");
            System.out.println("Lista de dispositivos no amb1");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            AddDispositivoNoEspaco(space,"amb1");
            lista=ListaDispositivos(space,"amb1");
            System.out.println("Lista de dispositivos no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            AddDispositivoNoEspaco(space,"amb2");
            lista=ListaDispositivos(space,"amb2");
            System.out.println("Lista de dispositivos no amb2");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            AddDispositivoNoEspaco(space,"amb1");
            lista=ListaDispositivos(space,"amb1");
            System.out.println("Lista de dispositivos no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            
          //teste de troca de ambiente
            DispositivoTrocaDeAmbiente(space,"disp2","amb2");
            
            lista=ListaDispositivos(space,"amb1");
            System.out.println("Lista de dispositivos no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            
            lista=ListaDispositivos(space,"amb2");
            System.out.println("Lista de dispositivos no amb2");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            
            
            //teste remoção dispositivo
            RemoveDispositivo(space,"disp1");
            lista=ListaDispositivos(space,"amb1");
            System.out.println("Lista de dispositivos no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Dispositivos)lista.get(i)).nomeDispositivo);
    		}
            
            
            
            //System.out.println("\nCondição de remoção do amb1 é "+PodeRemoverAmbiente(space,"amb1"));
            //teste usuarios
            System.out.println("\nPassou de linha\n");
            AddUsuarioNoEspaco(space,"amb1");
            lista=ListaUsuarios(space,"amb1");
            System.out.println("Lista de usuários no amb1");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            AddUsuarioNoEspaco(space,"amb1");
            lista=ListaUsuarios(space,"amb1");
            System.out.println("Lista de usuários no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            AddUsuarioNoEspaco(space,"amb2");
            lista=ListaUsuarios(space,"amb2");
            System.out.println("Lista de usuários no amb2");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            AddUsuarioNoEspaco(space,"amb1");
            lista=ListaUsuarios(space,"amb1");
            System.out.println("Lista de usuários no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            //teste de troca de ambiente
            UsuarioTrocaDeAmbiente(space,"user2","amb2");
            
            lista=ListaUsuarios(space,"amb1");
            System.out.println("Lista de usuários no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            
            lista=ListaUsuarios(space,"amb2");
            System.out.println("Lista de usuários no amb2");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            
            //teste remoção usuario
            RemoveUsuario(space,"user1");
            lista=ListaUsuarios(space,"amb1");
            System.out.println("Lista de usuários no amb1");
            for (int i = 0; i < lista.size(); i++) {
            	System.out.println(((Usuario)lista.get(i)).nomeUsuario);
    		}
            
            //teste de remoçao de ambiente que não pode ser removido
            RemoveAmbiente(space,"amb1");
            lista=ListaAmbientes(space);
            System.out.println("Todos os ambientes serão listados abaixo");
            for (int i = 0; i < lista.size(); i++) {
    			System.out.println(((Ambiente)lista.get(i)).nomeAmbiente);
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
