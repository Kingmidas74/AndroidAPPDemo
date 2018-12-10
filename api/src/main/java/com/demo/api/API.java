package com.demo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class API {
    private final RequestWrapper requestWrapper = new RequestWrapper();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer())/*.setDateFormat("yyyy-MM-dd HH:mm:ss")*/.create();

    private Integer UserId;
    private String Hash;




    public API() {
    }

    public API(Integer userId, String hash) {
        UserId=userId;
        Hash=hash;
    }

    public User TryLoginUser(String login, String password) throws IOException {
        String result = requestWrapper.MakeRequest("users?login="+login+"&password="+password,RequestMethods.POST,new ArrayList<RequestHeader>(),null);
        if(result.isEmpty()) throw new InvalidObjectException("user");
        User user = this.gson.fromJson(result,User.class);
        if(user.Id<2) throw new InvalidObjectException("user");
        return user;
    }

    public Boolean CreateUser(String email,String password, String repassword,String name,String surname) throws IOException {
        JsonObject jo = new JsonObject();
        jo.add("EMail", this.gson.toJsonTree(email));
        jo.add("Name", this.gson.toJsonTree(name));
        jo.add("Surname", this.gson.toJsonTree(surname));
        jo.add("Repassword", this.gson.toJsonTree(password));
        jo.add("Password", this.gson.toJsonTree(repassword));
        jo.add("organization",this.gson.toJsonTree("GUEST"));
        String message = jo.toString();
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("Accept-Language","ru"));
        headers.add(new RequestHeader("Content-Type","application/json"));
        String result = requestWrapper.MakeRequest("users",RequestMethods.POST,headers,message);
        if(!requestWrapper.IsSuccessRequest) throw new InvalidObjectException("user");
        return true;
    }

    public ArrayList<Node> LoadPreviewNodes(Integer taskId, String nodeName) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        if(nodeName.length()>0) {
            headers.add(new RequestHeader("x-node-name",nodeName));
        }
        headers.add(new RequestHeader("x-load-preview","true"));
        String result = requestWrapper.MakeRequest("tasks/"+taskId+"preview/info",RequestMethods.GET,headers,null);

        if(result.isEmpty()) throw new InvalidObjectException("previewNodes");
        Type listofscenesType = new TypeToken<ArrayList<Node>>(){}.getType();
        return gson.fromJson(result,listofscenesType);
    }

    public Boolean RestPassword(String email) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("Accept-Language","ru"));
        String result = requestWrapper.MakeRequest("users/"+email+"/passwords",RequestMethods.POST,headers,null);
        if(!requestWrapper.IsSuccessRequest) throw new InvalidObjectException("restPassword");
        return true;
    }

    public User LoadUser() throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("users/"+UserId,RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("user");
        User user = this.gson.fromJson(result,User.class);
        if(!Objects.equals(user.Id, UserId)) throw new InvalidObjectException("user");
        return user;
    }

    public ArrayList<Scene> LoadScenes() throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("scenes",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("scenes");
        Type listofscenesType = new TypeToken<ArrayList<Scene>>(){}.getType();
        return gson.fromJson(result,listofscenesType);
    }

    public RenderSettings GetTaskSettings(Integer taskId) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("tasks/"+taskId+"/settings",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("task settins");
        return this.gson.fromJson(result,RenderSettings.class);
    }

    public ArrayList<Task> LoadTasks(Integer sceneId) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("scenes/"+sceneId.toString()+"/tasks",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("tasks");
        Type listoftasksType = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> resultArray = new ArrayList<Task>();
        for (Task task:(ArrayList<Task>)(gson.fromJson(result,listoftasksType))) {
            if(task.TaskType.equals(TaskTypes.RENDER) || task.TaskType.equals(TaskTypes.DISTRIBUTED))
            {
                resultArray.add(task);
            }
        }
        return resultArray;
    }

    public Task CreateTask(Integer sceneId, RenderSettings renderSettings) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        headers.add(new RequestHeader("Content-Type","application/json"));
        String result = requestWrapper.MakeRequest("scenes/"+sceneId.toString()+"/tasks",RequestMethods.POST,headers,gson.toJson(renderSettings));
        if(result.isEmpty()) throw new InvalidObjectException("create task");
        return gson.fromJson(result,Task.class);
    }

    public ArrayList<String> LoadLogs(Integer sceneId) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("scenes/"+sceneId.toString()+"/logs",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("logs");
        Type listoftasksType = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> resultArray = new ArrayList<String>();
        for (String log:(ArrayList<String>)(gson.fromJson(result,listoftasksType))) {
            resultArray.add(log);
        }
        return resultArray;
    }

    public ArrayList<Task> LoadSpents() throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("tasks/history",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("spents");
        Type listoftasksType = new TypeToken<ArrayList<Task>>(){}.getType();
        return gson.fromJson(result,listoftasksType);
    }

    public ArrayList<Payment> LoadPayments() throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("history/payments",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("payments");
        Type listoftasksType = new TypeToken<ArrayList<Payment>>(){}.getType();
        return gson.fromJson(result,listoftasksType);
    }

    public ArrayList<Archive> LoadArchives() throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("files/archives",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("archives");
        Type listofscenesType = new TypeToken<ArrayList<Archive>>(){}.getType();
        return gson.fromJson(result,listofscenesType);
    }

    public void RemoveArchive(Archive archive) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        requestWrapper.MakeRequest("files/archives/"+archive.Title+"/",RequestMethods.DELETE,headers,null);
    }

    public Scene CreateScene(Archive archive) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        headers.add(new RequestHeader("x-file-name",archive.Title));
        String result = requestWrapper.MakeRequest("scenes",RequestMethods.POST,headers,null);
       // String result = "";
        if(result.isEmpty()) throw new InvalidObjectException("archives");
        return gson.fromJson(result,Scene.class);
    }

    public RenderSettings GetSceneSettings(Integer sceneId) throws IOException {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("scenes/"+sceneId+"/settings",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("task settins");
        return this.gson.fromJson(result,RenderSettings.class);
    }

    public Double ConvertMoneyToMinutes(Double money) throws IOException  {
        if(money.intValue()==0) return 0.0;
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        String result = requestWrapper.MakeRequest("payments/minutes/money/"+money.intValue()+"/",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("money to minutes converter");
        return this.gson.fromJson(result,Double.class);
    }

    public Double ConvertMinutesToMoney(Double minutes) throws IOException  {
        if(minutes.intValue()==0) return 0.0;
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));

        String result = requestWrapper.MakeRequest("payments/money/minutes/"+(minutes.intValue())+"/",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("money to minutes converter");
        return this.gson.fromJson(result,Double.class);
    }

    public Rate GetRate() throws IOException  {
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));

        String result = requestWrapper.MakeRequest("payments/rate",RequestMethods.GET,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("rate");
        Rate rate = this.gson.fromJson(result,Rate.class);
        rate.EURRUB =rate.EURRUB*0.95;
        rate.USDRUB =rate.USDRUB*0.95;
        return rate;
    }

    public String SendPayment(String currency, Double amount, Double rate, String paymentSystem) throws Exception {
        if(currency.isEmpty() || amount<=0 || rate<=0 || paymentSystem.isEmpty()) {
            throw new Exception("send payment params failed");
        }
        ArrayList<RequestHeader> headers = new ArrayList<RequestHeader>();
        headers.add(new RequestHeader("x-user-auth-key",UserId+","+Hash));
        headers.add(new RequestHeader("x-currency",currency));
        headers.add(new RequestHeader("x-amount",amount.toString()));
        headers.add(new RequestHeader("x-rate",rate.toString()));
        headers.add(new RequestHeader("x-payment-system",paymentSystem.toUpperCase()));

        String result = requestWrapper.MakeRequest("payments",RequestMethods.POST,headers,null);
        if(result.isEmpty()) throw new InvalidObjectException("send payment");
        PaymentResponseType response = this.gson.fromJson(result,PaymentResponseType.class);
        return response.result.url;
    }
}
