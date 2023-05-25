package com.lwm.springcloud.controller;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import hex.*;

@Controller
public class HexController {
	
	private Map<String,Player> map=new HashMap<String,Player>();
	
    
    @RequestMapping(value = "/hex")
    public String gethex(){
        return "hex";
    }
	
    @RequestMapping("/land")  
    String land(HttpServletRequest request) {   
        return "MyHtml";  
    }  
    
    @RequestMapping(value = "/begain",method = RequestMethod.POST)
    @ResponseBody
    public void  begainAI(HttpServletRequest request){
	String username=request.getParameter("username");
			int team=Integer.parseInt(request.getParameter("team"));
			int dif=Integer.parseInt(request.getParameter("diff"));
			Player ai=new AI(3,11);
			ai.setDepth(dif);
			ai.setTeam(team);
			map.put(username, ai);
    }
	
    @RequestMapping(value = "/undo",method = RequestMethod.POST)
    @ResponseBody
    public void  undo(HttpServletRequest request){
		String username=request.getParameter("username");
		System.out.println(username);
		map.get(username).undoCalled();
    }
    
    @RequestMapping(value = "/move",method = RequestMethod.POST)
    @ResponseBody
    public String move(HttpServletRequest request){
		String username=request.getParameter("username");
		String temp[]=request.getParameter("id").split(",");
		int x=Integer.parseInt(temp[0]);
		int y=Integer.parseInt(temp[1]);
		Point point=new Point(x,y);
		Point AImove=map.get(username).setMove(point);
		return AImove.x+","+AImove.y;
    }


//    @Autowired
//    private DeptService deptService;
//    @Autowired
//    private DiscoveryClient client;
//
//    @RequestMapping(value = "/dept/add",method = RequestMethod.POST)
//    public boolean add(@RequestBody Dept dept){
//        return deptService.addDept(dept);
//    }
//
//    @RequestMapping(value = "/dept/get/{id}",method = RequestMethod.GET)
//    public Dept get(@PathVariable("id")Long id){
//        return deptService.findById(id);
//    }


    
//    @RequestMapping(value = "/dept/test")
//    public Dept test(){
//    	Long a=2L;
//        return deptService.findById(a);
//    }
//    
//    @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
//    public List<Dept> list() throws IOException{
//
//        return deptService.findAll();
//    }
//
//    @RequestMapping(value = "dept/discovery", method = RequestMethod.GET)
//    public Object discovery(){
//        List<String> list = client.getServices();
//        System.out.println("********************" + list);
//        List<ServiceInstance> instances = client.getInstances("MICROSERVICECLOUD-DEPT");
//        for (ServiceInstance instance: instances) {
//            System.out.println(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
//        }
//        return this.client;
//    }

}
