public class ServerFacade {

    String serverURL = "";
    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    public void register(String... params) {
        String path = "/user";
        //this.makeRequest("POST", path);
    }
}
