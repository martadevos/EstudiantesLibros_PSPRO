public class Estudiante implements Runnable{
    @Override
    public void run() {
        try {
            for (;;) {
                int libroElegido1 = (int) (Math.random()*9+1), libroElegido2;
                do{
                    libroElegido2 = (int) (Math.random()*9+1);
                }while (libroElegido1 == libroElegido2);
                cogerLibro1Y2((libroElegido1-1), (libroElegido2-1));
                System.out.println(Thread.currentThread().getName() + " elige los libros: " + libroElegido1 + " y " + libroElegido2);
                Thread.sleep((long) (Math.random()*2001+3000));
                sueltaLibros((libroElegido1-1), (libroElegido2-1));
                System.out.println(Thread.currentThread().getName() + " ha dejado los libros: " + libroElegido1 + " y " + libroElegido2);
                Thread.sleep(1000);
            }
        } catch (

                InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void cogerLibro1Y2(int libroElegido1, int libroElegido2) {
        while (Libros.getListaLibros()[libroElegido1] || Libros.getListaLibros()[libroElegido2]) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        cambiaEstado(libroElegido1, libroElegido2);
    }

    public synchronized void sueltaLibros(int libroElegido1, int libroElegido2) {
        cambiaEstado(libroElegido1, libroElegido2);
        this.notifyAll();
    }

    public synchronized void cambiaEstado (int libroElegido1, int libroElegido2){
        Libros.getListaLibros()[libroElegido1] = !Libros.getListaLibros()[libroElegido1];
        Libros.getListaLibros()[libroElegido2] = !Libros.getListaLibros()[libroElegido2];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(new Estudiante());
            thread.setName("estudiante" + (i+1));
            thread.setPriority((i+1) * 2);
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
