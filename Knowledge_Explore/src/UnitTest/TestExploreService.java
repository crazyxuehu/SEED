package UnitTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.graduate.server.common.DataLoad;
import com.graduate.server.model.Entity;
import com.graduate.server.model.Feature;
import com.graduate.server.service.ExploreService;
import com.graduate.server.service.IndexService;
import com.graduate.server.service.SearchService;

public class TestExploreService extends BaseTest{
	@Resource SearchService service;
	@Resource ExploreService exservice;
	@Resource IndexService inservice;
	@Test
	public void testSimResult() {
		DataLoad db=new DataLoad();
		Scanner sc =new Scanner(System.in);
		List<String>list = new ArrayList<String>();
		List<String>queryEntity = new ArrayList<String>();
		List<String>featureList = new ArrayList<String>();
		List<String>relationList = new ArrayList<String>();
		List<Integer>directionList = new ArrayList<Integer>();
		List<Entity>entityList = null;
		while(true){
			System.out.println("input the search entity and end with -1");
			while(true) {
				String name = sc.nextLine();
				System.out.println(name);
				if(name.equals("-1")) break;
				else
				name=inservice.autoComplete(name, 6).get(0);//get auto complete here
				list.add(name);
				System.out.println("finish add");
			}
			if(list.size()>0) {
				for(String entity:list) {
					if(entity.contains("#")) {
						String [] entitySet = entity.split("##");
						featureList.add(entitySet[1]);
						relationList.add(entitySet[0]);
						directionList.add(0);
					}else {
						queryEntity.add(entity);
					}
				}
				entityList=service.getQueryEntity(queryEntity);
				List<Feature>featureSet=service.getFeature(featureList,directionList,relationList,null);
				System.out.println(entityList.size());
				for(Entity entity:entityList) {
					System.out.println("entity:"+entity.getName());
				}
				List<Entity>simList=exservice.getSimEntity(entityList,featureSet);//get similar entity
				for(Entity entity:simList) {
					System.out.println(entity.getName()+" "+entity.getScore());
				}
				List<Feature>simFeaturelist=service.getQueryFeature(simList);//get query feature
				for(int i=0;i<simFeaturelist.size();i++){
					System.out.println(simFeaturelist.get(i).getTarget().getName()+" "+simFeaturelist.get(i).getScore()+" "+simFeaturelist.get(i).getRelation().getDirection());
				}
			}
			list.clear();
		}
		
	}
}
