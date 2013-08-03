package com.xunlei.cms;

public class PagetransformBoImpl{
	
}
//package com.xunlei.owpp.bo;
//
//import static com.xunlei.owpp.util.OWPPFunctionConstant.QX_GAMEID;
//import static com.xunlei.owpp.util.OWPPFunctionConstant.YX_GAMEID;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.apache.velocity.Template;
//import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.VelocityEngine;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.simpleframework.xml.Serializer;
//import org.simpleframework.xml.core.Persister;
//
//import com.xunlei.common.dao.IAdvsDao;
//import com.xunlei.common.dao.IPlacardsDao;
//import com.xunlei.common.util.DatetimeUtil;
//import com.xunlei.common.util.PagedFliper;
//import com.xunlei.common.web.bean.DataAccessReturn;
//import com.xunlei.common.util.StringTools;
//import com.xunlei.common.util.XLRuntimeException;
//import com.xunlei.common.vo.Advs;
//import com.xunlei.common.vo.Placards;
//import com.xunlei.owpp.common.SyncScriptUtil;
//import com.xunlei.owpp.dao.ICaptureDao;
//import com.xunlei.owpp.dao.IGamesDao;
//import com.xunlei.owpp.dao.IGameserversstatusDao;
//import com.xunlei.owpp.dao.IResourcesDao;
//import com.xunlei.owpp.dao.ITemplatesDao;
//import com.xunlei.owpp.dao.IVoteDao;
//import com.xunlei.owpp.dao.IWallpapersDao;
//import com.xunlei.owpp.facade.IFacade;
//import com.xunlei.owpp.util.ComparatorGameserversDisplayorder;
//import com.xunlei.owpp.util.ComparatorGameserversOnline;
//import com.xunlei.owpp.util.Miscellaneous;
//import com.xunlei.owpp.util.OWPPConfigUtil;
//import com.xunlei.owpp.vo.Capturecontent;
//import com.xunlei.owpp.vo.GameServersInfo;
//import com.xunlei.owpp.vo.Games;
//import com.xunlei.owpp.vo.Gameservers;
//import com.xunlei.owpp.vo.Gameserversstatus;
//import com.xunlei.owpp.vo.Resources;
//import com.xunlei.owpp.vo.Templates;
//import com.xunlei.owpp.vo.Topgame;
//import com.xunlei.owpp.vo.Usercontributionn;
//import com.xunlei.owpp.vo.Wallpapers;
//import com.xunlei.owpp.vo.WebgameServerInfo;
//import com.xunlei.owpp.web.model.TopinfoManagedBean;
//
//public class PagetransformBoImpl extends BaseBo implements IPagetransformBo {
//	protected static final Logger logger = Logger.getLogger("com.xunlei.owpp.bo.PagetransformBoImpl");
//
//	private static final int BUFFERSIE = 4 * 1024;
//
//	private static final int LIST_PRE_PAGES = 3;
//
//	private static final int LIST_POS_PAGES = 3;
//
//	private static final int GAME_DOWNLOAD_CIDS_START = 4001;
//
//	private static final String GAME_DOWNLOAD_CIDS_FILE = "gamedownloadcids/gamedownloadcids.txt";
//
//	private ITemplatesDao templatesDao;
//
//	private IGamesDao gamesDao;
//
//	private IVoteDao voteDao;
//
//	private IResourcesDao resourcesDao;
//
//	private IWallpapersDao wallpapersDao;
//
//	private String domain = "";
//
//	private String img_domain = "";
//
//	private static final String IMG_PREFIX = "img.";
//
//	private static final String HTML_POSTFIX = ".html";
//
//	private String templateUploadPath = "";
//
//	private IPlacardsDao placardsDao;
//
//	private IAdvsDao advsDao;
//
//	private IGameserversBo gameserversBo;
//
//	private IGameserversstatusDao gameserversstatusDao;
//
//	private ICaptureDao captureDao;
//
//	private String contextpath = "";
//
//	private boolean isAll = false;
//
//	private static Map<String, ProcessBuilder> pbMap = new HashMap<String, ProcessBuilder>();
//
//	private static Map<String, Process> pcMap = new HashMap<String, Process>();
//
//	@Override
//	public int pagetransform(String gameid, String[] templateclasses) {
//		throw new UnsupportedOperationException();
//	}
//
//	@Override
//	public synchronized int pagetransform(String gameid, String contextpath, boolean isAll) throws Exception {
//		// if(gameid.equals("000012")){//fw��ֵ����
//		// IPaihangBo bo = ((IPaihangBo)new
//		// ClassPathXmlApplicationContext("../applicationContext_paihang.xml"
//		// ).getBean("paihangBo"));
//		// bo.fetchAndStore(null);
//		// }
//		this.isAll = isAll;
//		this.contextpath = contextpath;
//		Games tg = gamesDao.getGameInfoById(gameid);
//		domain = "http://" + tg.getDomain();
//		img_domain = "http://" + IMG_PREFIX + tg.getDomain();
//		templateUploadPath = OWPPConfigUtil.getTemplateUploadPath() + gameid + File.separator;
//		List<Templates> temmplatesList = templatesDao.queryList(gameid);
//		int rtn = OK;
//		try {
//			for (Templates template : temmplatesList) {
//				doTransform(template);
//			}
//			transformGameDowdloadcidsFile(gameid, tg.getDomain());
//			transformImgs(gameid, tg.getDomain());
//			transformServersInfo(tg);
//			SyncScriptUtil.rsync(tg.getGameid(), OWPPConfigUtil.getSynScriptPath());
//		}catch (Exception e) {
//			logger.error(e, e);
//			rtn = FAIL;
//			e.printStackTrace();
//			throw e;
//		}
//		return rtn;
//	}
//
//	/*
//	 * private void transformGameinfo(String gameid) throws IOException{
//	 * if(!gameid.equals("000020")){ return ; } List<Games> list =
//	 * IFacade.INSTANCE.getAllGamesNameWithChooseflag(true); JSONArray jsonarray =
//	 * new JSONArray(); for (Games game : list) { JSONObject jsonObject = new
//	 * JSONObject(game); jsonObject.remove("datasource");
//	 * jsonarray.put(jsonObject); }
//	 * Miscellaneous.writeStringFile(OWPPConfigUtil.getNGServersInfoJsTemplateFilesName() +
//	 * "games.js", jsonarray.toString()); }
//	 */
//	/**
//	 * �������cid�ļ�
//	 */
//	private void transformGameDowdloadcidsFile(String gameid, String domain) throws Exception {
//		if (!gameid.equals("000020")) {
//			return;
//		}
//		String htdocs = OWPPConfigUtil.getWebsiteFilesPath();
//		String filepath = htdocs + GAME_DOWNLOAD_CIDS_FILE;
//		File file = new File(filepath);
//		Miscellaneous.createFile(filepath);
//
//		PrintWriter pw = new PrintWriter(file);
//		int serialnum = GAME_DOWNLOAD_CIDS_START;
//		List<Resources> reslist = resourcesDao.getResourcesList(-1, null, null);
//		for (Resources res : reslist) {
//			if (isNotEmpty(res.getRescid())) {
//				pw.print(serialnum + " " + res.getRescid());
//				pw.println();
//				serialnum++;
//			}
//		}
//		pw.flush();
//		pw.close();
//		reslist.clear();
//		reslist = null;
//	}
//
//	/**
//	 * ��ɷ�������Ϣjs
//	 * 
//	 * @param game
//	 */
//	private void transformServersInfo(Games game) throws Exception {
//		Gameservers data = new Gameservers();
//		data.setGameid(game.getGameid());
//		PagedFliper fliper = new PagedFliper();
//		fliper.setSortColumn(" DisplayOrder asc, ServerId asc ");
//		fliper.setPageSize(10000);
//		DataAccessReturn<Gameservers> sheet = gameserversBo.queryGameservers(data, fliper);
//		if (Games.GAME_NETGAME_CLASS_NO.equals(game.getClassno())) {
//			transformNetgameServerInfo(game, sheet);
//		}else if (Games.GAME_WEBGAME_CLASS_NO.equals(game.getClassno())) {
//			transformWebgameServerInfo(game, sheet);
//		}else {
//			logger.info("��ͨ����Ϸ���ͣ�����ɷ�������Ϣ��");
//			return;
//		}
//	}
//
//	private void transformNetgameServerInfo(Games game, DataAccessReturn<Gameservers> sheet) throws Exception {
//		String filepath = OWPPConfigUtil.getNGServersInfoJsFilesPath() + game.getGameid();
//		transformServersInfoJs(game, sheet, filepath);
//		transformServersInfoXML(game, sheet, filepath);
//	}
//
//	@SuppressWarnings("unchecked")
//	private void transformWebgameServerInfo(Games game, DataAccessReturn<Gameservers> sheet) throws Exception {
//		String gameid = game.getGameid();
//		String jsString = "";
//		Collection<Gameservers> gsList = sheet.getDatas();
//		if (QX_GAMEID.equals(gameid)) {
//			jsString = getGameserverQXJS(gsList);
//		}else if (YX_GAMEID.equals(gameid)) {
//			jsString = getGameserverYXJS(gsList);
//		}else {
//			List<Gameservers> gameServersList = new ArrayList<Gameservers>();
//			for (Gameservers svr : gsList) {
//				Gameserversstatus gss = getGameserversstatusDao().queryGameserversstatusByGamidServerid(gameid, svr.getServerid());
//				if (gss != null) {
//					if (gss.getOnline() > 0) {
//						svr.setOnline((int) (gss.getOnline() * svr.getOnlinemultiple()));
//					}else {
//						svr.setOnline((int) (gss.getOnline()));
//					}
//					svr.setStatus(gss.getStatus());
//				}else {
//					svr.setOnline(Gameservers.STATUS_UNKNOWN);
//					svr.setStatus(Gameservers.STATUS_UNKNOWN);
//				}
//				gameServersList.add(svr);
//			}
//			// �������ʾ˳������������
//			Collections.sort((List) gameServersList, new ComparatorGameserversDisplayorder());// ����ʾ˳������
//			jsString = new WebgameServerInfo(gameServersList).toJSString();
//		}
//		Miscellaneous.writeStringFile(OWPPConfigUtil.getWGServersInfoJsFilesPath() + game.getGameid() + ".js", jsString);
//	}
//
//	@SuppressWarnings("unchecked")
//	private String getGameserverQXJS(Collection<Gameservers> gsList) throws Exception {
//		final String gameid = QX_GAMEID;
//		for (Gameservers svr : gsList) {
//			Gameserversstatus gss = getGameserversstatusDao().queryGameserversstatusByGamidServerid(gameid, svr.getServerid());
//			if (gss != null) {
//				if (gss.getOnline() > 0) {
//					svr.setOnline((int) (gss.getOnline() * svr.getOnlinemultiple()));
//				}else {
//					svr.setOnline((int) (gss.getOnline()));
//				}
//				svr.setStatus(gss.getStatus());
//			}else {
//				svr.setOnline(Gameservers.STATUS_UNKNOWN);
//				svr.setStatus(Gameservers.STATUS_UNKNOWN);
//			}
//		}
//		List<Gameservers> gameServers = new ArrayList<Gameservers>();
//		List<Gameservers> recommendGameServers = new ArrayList<Gameservers>();
//		Collections.sort((List) gsList, new ComparatorGameserversOnline());// �Է�����������������
//		for (Gameservers svr : gsList) {// �Ƽ�������Ϊ״̬Ϊ�Ƽ�������ά��״̬��������������500��
//			if (svr.getStatus() != 1 && svr.getOnline() < 500 && svr.getIsrecommand() == 1 && svr.getInuse() == 1 && recommendGameServers.size() < 4) {
//				svr.setOnline(-999);
//				recommendGameServers.add(svr);
//			}
//		}
//		Collections.sort((List) gsList, new ComparatorGameserversDisplayorder());// ����ʾ˳������
//		for (Gameservers svr : gsList) {
//			svr.setOnline(-999);
//			gameServers.add(svr);
//		}
//		return new WebgameServerInfo(gameServers, recommendGameServers).toJSString();
//	}
//
//	private String getGameserverYXJS(Collection<Gameservers> gsList) throws Exception {
//		final String gameid = YX_GAMEID;
//		List<Gameservers> gameServers = new ArrayList<Gameservers>();
//		List<Gameservers> recommendGameServers = new ArrayList<Gameservers>();
//		List<Gameservers> recommendServers = new ArrayList<Gameservers>();
//		List<Gameservers> recommendServersTmp = new ArrayList<Gameservers>();
//		for (Gameservers svr : gsList) {
//			Gameserversstatus gss = getGameserversstatusDao().queryGameserversstatusByGamidServerid(gameid, svr.getServerid());
//			if (gss != null) {
//				svr.setStatus(gss.getStatus());
//				if (gss.getOnline() > 0) {
//					svr.setOnline((int) (gss.getOnline() * svr.getOnlinemultiple()));
//				}else {
//					svr.setOnline((int) (gss.getOnline()));
//				}
//				// svr.setOnline((int) (gss.getOnline() *
//				// OWPPConfigUtil.getYXOnlineUpFloat()));// YX���������ϸ�
//			}else {
//				svr.setOnline(Gameservers.STATUS_UNKNOWN);
//				svr.setStatus(Gameservers.STATUS_UNKNOWN);
//			}
//			gameServers.add(svr);
//			if (svr.getStatus() != Gameservers.STATUS_UNKNOWN && svr.getStatus() != Gameservers.STATUS_SHUTDOWN && recommendServers.size() < 6) {
//				recommendServers.add(svr);
//			}
//			if (svr.getIsrecommand() == 1 && svr.getStatus() != Gameservers.STATUS_UNKNOWN && svr.getStatus() != Gameservers.STATUS_SHUTDOWN
//					&& !recommendServers.contains(svr)) {
//				recommendServersTmp.add(svr);
//			}
//		}
//		for (int i = 0; i < 6 - recommendServersTmp.size(); i++) {
//			if (recommendServers.size() > i) {
//				recommendGameServers.add(recommendServers.get(i));
//			}
//		}
//		for (Gameservers svr : recommendServersTmp) {
//			recommendGameServers.add(svr);
//		}
//		return new WebgameServerInfo(gameServers, recommendGameServers).toJSString();
//	}
//
//	private void transformServersInfoJs(Games game, DataAccessReturn<Gameservers> sheet, String filepath) throws Exception {
//		// ģ�����js
//		VelocityEngine velocityEngine = new VelocityEngine();
//		velocityEngine.setProperty("input.encoding", "utf-8");
//		velocityEngine.setProperty("output.encoding", "utf-8");
//		velocityEngine.setProperty("file.resource.loader.path", OWPPConfigUtil.getTemplateUploadPath() + "000020" + File.separator);
//		velocityEngine.init();
//		String templatename = "";
//		if (Games.GAME_NETGAME_CLASS_NO.equals(game.getClassno())) {
//			templatename = OWPPConfigUtil.getNGServersInfoJsTemplateFilesName();
//		}else if (Games.GAME_WEBGAME_CLASS_NO.equals(game.getClassno())) {
//			templatename = OWPPConfigUtil.getWGServersInfoJsTemplateFilesName();
//		}
//		Template template = velocityEngine.getTemplate(templatename);
//		StringWriter writer = new StringWriter();
//		HashMap<String, String> data = new HashMap<String, String>();
//		JSONArray jsonarray = new JSONArray();
//		for (Gameservers server : sheet.getDatas()) {
//			if (server.getOnline() > 0) server.setOnline((int) (server.getOnline() * server.getOnlinemultiple()));
//			jsonarray.put(new JSONObject(server));
//		}
//		data.put("serversinfo", jsonarray.toString());
//		VelocityContext context = new VelocityContext(data);
//		template.merge(context, writer);
//		String content = writer.toString();
//
//		File jsfile = new File(filepath + ".js");
//		Miscellaneous.createFile(filepath + ".js");
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsfile), "utf-8"));
//		bw.write(content);
//		bw.close();
//
//		// ���json
//		File jsonfile = new File(filepath + ".json");
//		Miscellaneous.createFile(filepath + ".json");
//		BufferedWriter jsonbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jsonfile), "utf-8"));
//		jsonbw.write(data.get("serversinfo"));
//		jsonbw.close();
//	}
//
//	private void transformServersInfoXML(Games game, DataAccessReturn<Gameservers> sheet, String filepath) throws Exception {
//		File xmlfile = new File(filepath + ".xml");
//		Miscellaneous.createFile(filepath + ".xml");
//
//		Serializer serializer = new Persister();
//		GameServersInfo serversinfo = new GameServersInfo();
//		for (Gameservers g : sheet.getDatas()) {
//			if (g.getOnline() > 0) g.setOnline((int) (g.getOnline() * g.getOnlinemultiple()));
//			serversinfo.add(g);
//		}
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlfile), "utf-8"));
//		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
//		serializer.write(serversinfo, bw);
//	}
//
//	/**
//	 * ��ָ��Ŀ¼��ͼƬת�Ƶ���̬·����
//	 * 
//	 * @param gameid
//	 * @param domain
//	 */
//	private void transformImgs(String gameid, String domain) {
//		String webroot = OWPPConfigUtil.getWebRootPath();
//		String upload = OWPPConfigUtil.getRootFilesPath();
//		String htdocs = OWPPConfigUtil.getWebsiteFilesPath();
//		// fck
//		String fcksrcdir = webroot + "upload/" + gameid + "/fck/image/";
//		String fcktgdir = htdocs + IMG_PREFIX + domain + "/fck/image/";
//		copyFiles(fcksrcdir, fcktgdir);
//		// adv
//		String advsrcdir = upload + "upload/" + gameid + "/advs/images/";
//		String advtgdir = htdocs + IMG_PREFIX + domain + "/advs/images/";
//		copyFiles(advsrcdir, advtgdir);
//		// placard
//		String plasrcdir = upload + "upload/" + gameid + "/placards/images/";
//		String platgdir = htdocs + domain + "/placards/images/";
//		copyFiles(plasrcdir, platgdir);
//	}
//
//	private void copyFiles(String src, String target) {
//		System.out.println(src + "->" + target);
//		File srcdir = new File(src);
//		if (!srcdir.exists() || !srcdir.isDirectory()) {
//			return;
//		}
//		File tgdir = new File(target);
//		if (!tgdir.exists()) {
//			tgdir.mkdirs();
//		}
//		try {
//			for (File f : srcdir.listFiles()) {
//				FileInputStream is = new FileInputStream(f);
//				File tf = new File(target + f.getName());
//				if (!tf.exists() || isAll) {// ���ȫ���������е�ͼƬ�������ֻ����Ŀ��Ŀ¼�²����ڵ�ͼƬ
//					if (tf.exists()) {
//						tf.delete();
//					}
//					tf.createNewFile();
//					FileOutputStream fos = new FileOutputStream(tf);
//					byte[] buffer = new byte[BUFFERSIE];
//					while (is.read(buffer) != -1) {
//						fos.write(buffer);
//					}
//					fos.close();
//				}
//				tf = null;
//				is.close();
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new XLRuntimeException("�����ļ�ʧ�ܣ�����ϵ����Ա��");
//		}
//	}
//
//	private void runSynScript(Games game) throws Exception {
//		logger.info("---------------- start to run sync script ----------------");
//		/*
//		 * if (!Games.GAME_NETGAME_CLASS_NO.equals(game.getClassno())) {
//		 * logger.info("�Ǵ������Σ�������ͬ���ű���"); return; }
//		 */
//		ProcessBuilder pb = null;
//		if (pbMap.containsKey(game.getGameid()) && null != pbMap.get(game.getGameid())) {
//			pb = pbMap.get(game.getGameid());
//			logger.info("process builder has existed on gameid=" + game.getGameid());
//		}else {
//			String scriptpath = OWPPConfigUtil.getSynScriptPath();
//			File synscriptfile = new File(scriptpath + "rsync_netgame_" + game.getGameid() + ".sh");
//			if (!synscriptfile.exists()) {
//				logger.info(synscriptfile.getPath() + ":ͬ���ű������ڣ�");
//				return;
//			}
//			String commond = "./rsync_netgame_" + game.getGameid() + ".sh";
//			pb = new ProcessBuilder(commond);
//			// pb = new ProcessBuilder("cmd.exe","/c
//			// d:/usr/local/bin/test.bat");
//			/* ���ý�̹���Ŀ¼ */
//			pb.directory(new File(scriptpath));
//			pb.redirectErrorStream(true);
//			System.out.println(pb.command());
//			logger.info("commond=" + pb.command());
//			pbMap.put(game.getGameid(), pb);
//		}
//		if (pcMap.containsKey(game.getGameid()) && null != pcMap.get(game.getGameid())) {
//			Process tmp = pcMap.get(game.getGameid());
//			pcMap.remove(game.getGameid());
//			if (null != tmp) {
//				tmp.destroy();
//				logger.info("destroy process on gameid=" + game.getGameid());
//			}
//			tmp = null;
//		}
//		logger.info("before start process");
//		Process pc = pb.start();
//		logger.info("start process success");
//		this.mgrProcess(pc);
//		pcMap.put(game.getGameid(), pc);
//		logger.info("---------------- end to run sync script ----------------");
//		// this.readConsole(pc);
//		// pcMap.put(game.getGameid(), pc);
//		// logger.info("start process success");
//		// InputStreamReader read = new InputStreamReader(pc.getInputStream());
//		// BufferedReader br = new BufferedReader(read);
//		// String line = null;
//		// while ((line = br.readLine()) != null){
//		// System.out.println(line);
//		// }
//		// int end = pc.waitFor();
//		// if(end != 0){
//		// logger.error("����ͬ���ű�ʧ�ܣ�");
//		// }
//	}
//
//	private void mgrProcess(Process process) {
//		try {
//			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				System.out.println(line);
//				logger.debug("running:" + line);
//			}
//			process.waitFor();
//		}catch (Exception e) {
//			logger.error(e, e);
//		}finally {
//			if (process != null) process.destroy();
//			logger.info("destroy process in finally");
//		}
//		if (process != null) process.destroy();
//		logger.info("destory process");
//	}
//
//	private String readConsole(Process process) throws IOException {
//		StringBuffer cmdout = new StringBuffer();
//		InputStream fis = process.getInputStream();
//		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//		String line = null;
//		while ((line = br.readLine()) != null) {
//			// cmdout.append(line).append(System.getProperty("line.separator"));
//			logger.debug("running..." + line);
//		}
//		// logger.info("ִ��ϵͳ�����Ľ��Ϊ��\n" + cmdout.toString());
//		return cmdout.toString().trim();
//	}
//
//	@Override
//	public int synchronizedPagesToServer(String gameid) {
//		throw new UnsupportedOperationException();
//	}
//
//	private void doTransform(Templates template) throws Exception {
//		Map<String, Object> config = parseTemplateConfig(template);
//		if (config.get("pageType").toString().equals("list")) {
//			getListDataFromDatabase(config, template);
//		}else if (config.get("pageType").toString().equals("content")) {
//			getContentDataFromDatabase(config, template);
//		}else if (config.get("pageType").toString().equals("desktoppic")) {
//			getDesktoppicDataFromDatabase(config, template);
//		}else {
//			Map<String, Object> data = getDataFromDatabase(config, template);
//			String path = config.get("staticPath").toString();
//			if (!path.endsWith("/")) {
//				generatePage(config, data);
//			}
//			data.clear();
//			data = null;
//		}
//
//	}
//
//	/**
//	 * ��һ�������ҳ
//	 * 
//	 * @param data
//	 * @param config
//	 * @param template
//	 * @throws Exception
//	 */
//	private void getUserContributionPage(Map<String, Object> data, Map<String, Object> config, Templates template) throws Exception {
//		Map<String, String> map = (Map<String, String>) config.get("userContribution");
//		if (map != null) {
//			String varName = map.get("varName");
//			String size = map.get("size");
//			String type = map.get("type");
//			if (isEmpty(varName) || isEmpty(size) || isEmpty(type)) {
//				throw new XLRuntimeException("Tag <userContribution> without (varName || size || type) Error");
//			}else {
//				PagedFliper fliper = new PagedFliper(Integer.valueOf(size));
//				fliper.setSortColumnIfEmpty(" orderidx ASC,seqid DESC ");
//
//				Usercontributionn uc = new Usercontributionn();
//				uc.setInuse(1);
//				uc.setArticlestate(1);
//
//				Map<String, List<Usercontributionn>> mapData = new HashMap<String, List<Usercontributionn>>();
//				List<Usercontributionn> list = null;
//				String[] types = type.split(",");
//				for (String str : types) {
//					uc.setArticletype(Integer.valueOf(str));
//					list = (List<Usercontributionn>) IFacade.INSTANCE.queryUsercontributionn(uc, fliper).getDatas();
//					logger.debug("userContribution type=" + str + ",size=" + list.size());
//					mapData.put("type" + str, list);
//				}
//				data.put(varName, mapData);
//			}
//		}
//
//		map = (Map<String, String>) config.get("userContributionPage");
//		if (map != null) {
//			String varName = map.get("varName");
//			String strPageSize = map.get("pageSize");
//			String staticPath = map.get("staticPath");
//			String postfix = map.get("postfix");
//			String strType = map.get("type");
//			if (isEmpty(varName) || isEmpty(strPageSize) || isEmpty(staticPath) || isEmpty(postfix) || isEmpty(strType)) {
//				throw new XLRuntimeException("Tag <userContributionPage> without (varName || pageSize || staticPath || postfix || type) Error");
//			}else {
//				int pageSize = Integer.valueOf(strPageSize);
//				
//				PagedFliper fliper = new PagedFliper();
//				fliper.setSortColumnIfEmpty(" orderidx ASC,seqid DESC ");
//
//				Usercontributionn uc = new Usercontributionn();
//				uc.setInuse(1);
//				uc.setArticlestate(1);
//
//				Map<String, Object> tmpMap = null;
//				List<Usercontributionn> list = null;
//				List<String> pages = null;
//				String[] types = strType.split(",");
//				for (String type : types) {
//					uc.setArticletype(Integer.valueOf(type));
//					list = (List<Usercontributionn>) IFacade.INSTANCE.queryUsercontributionn(uc, fliper).getDatas();
//					logger.debug("userContribution type=" + type + ",size=" + list.size());
//					
//					tmpMap = new HashMap<String, Object>();
//					pages = new ArrayList<String>();
//
//					tmpMap.put("type", type);
//					if (list.size() < 1) {
//						pages.add("1");
//						tmpMap.put("pageMax", 1);
//						tmpMap.put("pages", pages);
//						tmpMap.put("pageIndex", 1);
//						tmpMap.put("pageNext", 1);
//						tmpMap.put("pageProv", 1);
//						tmpMap.put("pageMin", 1);
//						config.put("staticPath", staticPath + type + "_1" + postfix);
//						tmpMap.put("listDatas", list);
//						data.put(map.get("varName"), tmpMap);
//						generatePage(config, data);
//						logger.debug("generatePage staticPath=" + config.get("staticPath"));
//					}else {
//						int pageMax = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//						tmpMap.put("pageMax", pageMax);
//						tmpMap.put("pageMin", 1);
//
//						int pageIndex = 1;
//						int pageSum = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//						for (int i = 1; i <= pageSum; i++)
//							pages.add(String.valueOf(i));
//						tmpMap.put("pages", pages);
//
//						List<Usercontributionn> cList = new ArrayList<Usercontributionn>();
//						for (pageIndex = 1; pageIndex <= pageSum; pageIndex++) {
//							tmpMap.put("pageIndex", pageIndex);
//							tmpMap.put("pageNext", pageIndex + 1);
//							tmpMap.put("pageProv", pageIndex - 1);
//							config.put("staticPath", staticPath + type + "_" + pageIndex + postfix);
//							cList.clear();
//							for (int i = (pageIndex - 1) * pageSize; i < (pageIndex * pageSize > list.size() ? list.size() : pageIndex * pageSize); i++) {
//								cList.add(list.get(i));
//							}
//							tmpMap.put("listDatas", cList);
//							data.put(varName, tmpMap);
//							generatePage(config, data);
//							logger.debug("generatePage staticPath=" + config.get("staticPath"));
//						}
//					}
//				}
//			}
//		}
//	}
//
//	/**
//	 * �Ż���Ϸ�б�
//	 * 
//	 * @param data
//	 * @param config
//	 * @param template
//	 * @throws Exception
//	 */
//	private void getIndexGameList(Map<String, Object> data, Map<String, Object> config, Templates template) throws Exception {
//		Map<String, String> map = (Map<String, String>) config.get("indexgamelist");
//		if (map != null) {
//			Map<String, List<Topgame>> mapData = TopinfoManagedBean.getGameListMap();
//			data.put(map.get("varName"), mapData.get("1-2"));
//			logger.debug("indexgamelist varName=" + map.get("varName"));
//		}
//
//		map = (Map<String, String>) config.get("pagegamelist");
//		if (map != null) {
//			Map<String, List<Topgame>> mapData = TopinfoManagedBean.getGameListMap();
//			String[] types = { "1-2", "1", "2" };
//			String varName = map.get("varName").toString();
//			String staticPath = map.get("staticPath").toString();
//			String postfix = map.get("postfix").toString();
//			int pageSize = Integer.parseInt(map.get("pageSize"));
//			logger.debug("pagegamelist varName=" + varName + ",staticPath=" + staticPath);
//
//			List<Topgame> list = null;
//			Map<String, Object> tmpMap = null;
//			List<String> pages = null;
//			for (String type : types) {
//				list = mapData.get(type);
//				tmpMap = new HashMap<String, Object>();
//				pages = new ArrayList<String>();
//
//				tmpMap.put("type", type);
//				if (list.size() < 1) {
//					pages.add("1");
//					tmpMap.put("pageMax", 1);
//					tmpMap.put("pages", pages);
//					tmpMap.put("pageIndex", 1);
//					tmpMap.put("pageNext", 1);
//					tmpMap.put("pageProv", 1);
//					tmpMap.put("pageMin", 1);
//					config.put("staticPath", staticPath + type + "_1" + postfix);
//					tmpMap.put("listDatas", list);
//					data.put(map.get("varName"), tmpMap);
//					generatePage(config, data);
//					logger.debug("generatePage staticPath=" + config.get("staticPath"));
//				}else {
//					int pageMax = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//					tmpMap.put("pageMax", pageMax);
//					tmpMap.put("pageMin", 1);
//
//					int pageIndex = 1;
//					int pageSum = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//					for (int i = 1; i <= pageSum; i++)
//						pages.add(String.valueOf(i));
//					tmpMap.put("pages", pages);
//
//					List<Topgame> cList = new ArrayList<Topgame>();
//					for (pageIndex = 1; pageIndex <= pageSum; pageIndex++) {
//						tmpMap.put("pageIndex", pageIndex);
//						tmpMap.put("pageNext", pageIndex + 1);
//						tmpMap.put("pageProv", pageIndex - 1);
//						config.put("staticPath", staticPath + type + "_" + pageIndex + postfix);
//						cList.clear();
//						for (int i = (pageIndex - 1) * pageSize; i < (pageIndex * pageSize > list.size() ? list.size() : pageIndex * pageSize); i++) {
//							cList.add(list.get(i));
//						}
//						tmpMap.put("listDatas", cList);
//						data.put(varName, tmpMap);
//						generatePage(config, data);
//						logger.debug("generatePage staticPath=" + config.get("staticPath"));
//					}
//				}
//
//			}
//		}
//	}
//
//	/**
//	 * ��ȡģ�����õ�ץȡ����
//	 */
//	private void getCapturecontent(Map<String, Object> data, Map<String, Object> config, Templates template) throws Exception {
//		// capturecontent
//		Map<String, String> map = (Map<String, String>) config.get("capturecontent");
//		// System.out.println("-- " + template.getGameid() + " map=" + map);
//		if (map != null) {
//			Capturecontent condition = new Capturecontent();
//			condition.setGameid(template.getGameid());
//			condition.setIstransform(-1);
//			PagedFliper fliper = null;
//			int len = Integer.valueOf(map.get("varLen").toString());
//			if (len != -1) {
//				fliper = new PagedFliper(len);
//			}
//			Capturecontent c = null;
//			List<Capturecontent> list = (List<Capturecontent>) captureDao.queryCapturecontentView(condition, fliper).getDatas();
//			for (int i = 0; i < list.size(); i++) {
//				c = list.get(i);
//				if (isNotEmpty(c.getInputtime())) c.setInputtime(c.getInputtime().substring(0, 11));
//			}
//
//			Map<String, Object> tmpMap = new HashMap<String, Object>();
//			// data.put(map.get("varName"), list);
//
//			List<String> pages = new ArrayList<String>();
//			String staticPath = map.get("staticPath").toString();
//			String type = map.get("type").toString();
//			String postfix = map.get("postfix").toString();
//			int pageSize = Integer.parseInt(map.get("pageSize"));
//			tmpMap.put("type", type);
//
//			if (list.size() < 1) {
//				pages.add("1");
//				tmpMap.put("pageMax", 1);
//				tmpMap.put("pages", pages);
//				tmpMap.put("pageIndex", 1);
//				tmpMap.put("pageNext", 1);
//				tmpMap.put("pageProv", 1);
//				tmpMap.put("pageMin", 1);
//				config.put("staticPath", staticPath + type + "_1" + postfix);
//				tmpMap.put("listDatas", list);
//				data.put(map.get("varName"), tmpMap);
//				generatePage(config, data);
//			}else {
//				int pageMax = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//				tmpMap.put("pageMax", pageMax);
//				tmpMap.put("pageMin", 1);
//
//				int pageIndex = 1;
//				int pageSum = list.size() % pageSize == 0 ? list.size() / pageSize : (list.size() / pageSize + 1);
//				for (int i = 1; i <= pageSum; i++)
//					pages.add(String.valueOf(i));
//				tmpMap.put("pages", pages);
//
//				List<Capturecontent> cList = new ArrayList<Capturecontent>();
//				for (pageIndex = 1; pageIndex <= pageSum; pageIndex++) {
//					tmpMap.put("pageIndex", pageIndex);
//					tmpMap.put("pageNext", pageIndex + 1);
//					tmpMap.put("pageProv", pageIndex - 1);
//					config.put("staticPath", staticPath + type + "_" + pageIndex + postfix);
//					cList.clear();
//					for (int i = (pageIndex - 1) * pageSize; i < (pageIndex * pageSize > list.size() ? list.size() : pageIndex * pageSize); i++) {
//						cList.add(list.get(i));
//					}
//					tmpMap.put("listDatas", cList);
//					data.put(map.get("varName"), tmpMap);
//					generatePage(config, data);
//				}
//			}
//			// System.out.println("-- " + len + " size=" + list.size());
//		}
//	}
//
//	/**
//	 * ����ģ�����׵������ļ� xml
//	 * 
//	 * @throws Exception
//	 */
//	private Map<String, Object> parseTemplateConfig(Templates template) throws Exception {
//		Map<String, Object> config = new HashMap<String, Object>();
//		config.put("templatePath", template.getTemplatefile());
//		// ����·��Ϊģ��·��+.xml
//		String configPath = templateUploadPath + template.getTemplatefile().split(";")[0] + ".xml";
//		SAXReader saxReader = new SAXReader();
//		Document configDoc = saxReader.read(new FileInputStream(new File(configPath)));
//		Element root = configDoc.getRootElement();
//		config.put("pageType", root.element("pageType").getText());
//		config.put("pageEncoding", root.element("pageEncoding").getText());
//		config.put("domainName", root.element("domainName").getText());
//		config.put("staticPath", root.element("staticPath").getText());
//		// listdata
//		Map<String, String> listData = new HashMap<String, String>();
//		Element tel2 = root.element("listPageData");
//		if (tel2 != null) {
//			listData.put("varName", tel2.element("varName").getText());
//			listData.put("varLen", tel2.element("varLen").getText());
//			listData.put("type", tel2.element("type").getText());
//			listData.put("pageSize", tel2.element("pageSize").getText());
//			if (null != tel2.element("postfix") && isNotEmpty(tel2.element("postfix").getText())) {
//				listData.put("postfix", "." + tel2.element("postfix").getText());
//			}else {
//				listData.put("postfix", HTML_POSTFIX);
//			}
//			config.put("listPageData", listData);
//		}
//		// contentdata
//		Map<String, String> contentData = new HashMap<String, String>();
//		Element tel = root.element("contentPageData");
//		if (tel != null) {
//			contentData.put("varName", tel.element("varName").getText());
//			contentData.put("varLen", tel.element("varLen").getText());
//			contentData.put("type", tel.element("type").getText());
//			if (null != tel && null != tel.element("postfix") && isNotEmpty(tel.element("postfix").getText())) {
//				contentData.put("postfix", "." + tel.element("postfix").getText());
//			}else {
//				contentData.put("postfix", HTML_POSTFIX);
//			}
//			if(tel.element("listType") != null) {
//				contentData.put("listType", tel.element("listType").getText());
//			} else {
//				contentData.put("listType", null);
//			}
//			config.put("contentData", contentData);
//		}
//		// placard
//		List<Map<String, String>> placardList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("placard")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("varLen", el.element("varLen").getText());
//			tMap.put("type", el.element("type").getText());
//
//			if (null != el && null != el.element("postfix") && isNotEmpty(el.element("postfix").getText())) {
//				tMap.put("postfix", "." + el.element("postfix").getText());
//			}else {
//				tMap.put("postfix", HTML_POSTFIX);
//			}
//			placardList.add(tMap);
//		}
//		config.put("placardList", placardList);
//		// adv
//		List<Map<String, String>> advList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("adv")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("varLen", el.element("varLen").getText());
//			tMap.put("type", el.element("type").getText());
//			advList.add(tMap);
//		}
//		config.put("advList", advList);
//		// resource
//		List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("resource")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("varLen", el.element("varLen").getText());
//			tMap.put("type", el.element("type").getText());
//			resList.add(tMap);
//		}
//		config.put("resList", resList);
//		// wallpapers
//		List<Map<String, String>> wallpaperList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("wallpaper")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("varLen", el.element("varLen").getText());
//			wallpaperList.add(tMap);
//		}
//		config.put("wallpaperList", wallpaperList);
//
//		// include
//		List<Map<String, String>> includeList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("include")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("path", el.element("path").getText());
//			tMap.put("encoding", el.element("encoding").getText());
//			includeList.add(tMap);
//		}
//		config.put("includeList", includeList);
//
//		// games
//		List<Map<String, String>> gameList = new ArrayList<Map<String, String>>();
//		for (Object elObj : root.elements("games")) {
//			Element el = (Element) elObj;
//			Map<String, String> tMap = new HashMap<String, String>();
//			tMap.put("varName", el.element("varName").getText());
//			tMap.put("varLen", el.element("varLen").getText());
//			tMap.put("type", el.element("type").getText());
//			gameList.add(tMap);
//		}
//		config.put("gamesList", gameList);
//
//		// capturecontent
//		Map<String, String> capturecontent = new HashMap<String, String>();
//		Element e = root.element("capturecontent");
//		if (e != null) {
//			capturecontent.put("varName", e.element("varName").getText());
//			capturecontent.put("varLen", e.element("varLen").getText());
//			capturecontent.put("pageSize", e.element("pageSize").getText());
//			capturecontent.put("staticPath", e.element("staticPath").getText());
//			capturecontent.put("type", e.element("type").getText());
//			capturecontent.put("postfix", e.element("postfix").getText());
//			config.put("capturecontent", capturecontent);
//		}
//
//		// �Ż���Ϸ�б�
//		Map<String, String> gamelist = null;
//		e = root.element("indexgamelist");
//		if (e != null) {
//			gamelist = new HashMap<String, String>();
//			gamelist.put("varName", e.element("varName").getText());
//			config.put("indexgamelist", gamelist);
//		}
//		e = root.element("pagegamelist");
//		if (e != null) {
//			gamelist = new HashMap<String, String>();
//			gamelist.put("varName", e.element("varName").getText());
//			gamelist.put("staticPath", e.element("staticPath").getText());
//			gamelist.put("pageSize", e.element("pageSize").getText());
//			gamelist.put("postfix", e.element("postfix").getText());
//			config.put("pagegamelist", gamelist);
//		}
//		
//		// user contribution
//		gamelist = null;
//		e = root.element("userContribution");
//		if (e != null) {
//			gamelist = new HashMap<String, String>();
//			gamelist.put("varName", e.element("varName").getText());
//			gamelist.put("size", e.element("size").getText());
//			gamelist.put("type", e.element("type").getText());
//			config.put("userContribution", gamelist);
//		}
//		e = root.element("userContributionPage");
//		if (e != null) {
//			gamelist = new HashMap<String, String>();
//			gamelist.put("varName", e.element("varName").getText());
//			gamelist.put("pageSize", e.element("pageSize").getText());
//			gamelist.put("staticPath", e.element("staticPath").getText());
//			gamelist.put("postfix", e.element("postfix").getText());
//			gamelist.put("type", e.element("type").getText());
//			config.put("userContributionPage", gamelist);
//		}
//		return config;
//	}
//
//	/**
//	 * ��ݽ������xml���ö�ȡ��ݿ���� ����velocity�����context��
//	 * 
//	 * @param config
//	 * @return
//	 */
//	private Map<String, Object> getDataFromDatabase(Map<String, Object> config, Templates template) throws Exception {
//		Map<String, Object> data = new HashMap<String, Object>();
//		// placard
//		List<Map<String, String>> plaList = (List<Map<String, String>>) config.get("placardList");
//		for (Map<String, String> tMap : plaList) {
//			List<Placards> tList = getPlacardsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"), true);
//			for (Placards tp : tList) {
//				if (tp.getContentflag() == 0) {
//					tp.setPlacardslinkurl(domain + "/c/" + tp.getPlacardid() + tMap.get("postfix"));
//				}else {
//					tp.setPlacardslinkurl(tp.getPlacardcontent());
//				}
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// adv
//		List<Map<String, String>> advList = (List<Map<String, String>>) config.get("advList");
//		for (Map<String, String> tMap : advList) {
//			List<Advs> tList = getAdvsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			data.put(tMap.get("varName"), tList);
//		}
//		// include
//		List<Map<String, String>> includeList = (List<Map<String, String>>) config.get("includeList");
//		for (Map<String, String> tMap : includeList) {
//			String script = getInclude(tMap.get("path"), tMap.get("encoding"));
//			data.put(tMap.get("varName"), script);
//		}
//		// resource
//		List<Map<String, String>> resList = (List<Map<String, String>>) config.get("resList");
//		for (Map<String, String> tMap : resList) {
//			List<Resources> tList = resourcesDao.getResourcesList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			for (Resources tr : tList) {
//				setThunderUrl(tr, "#");
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// wallpaper
//		List<Map<String, String>> wallpaperList = (List<Map<String, String>>) config.get("wallpaperList");
//		for (Map<String, String> tMap : wallpaperList) {
//			List<Wallpapers> tList = wallpapersDao.getWallpapersList(Integer.parseInt(tMap.get("varLen")), template.getGameid());
//			data.put(tMap.get("varName"), tList);
//		}
//		// games
//		List<Map<String, String>> gamesList = (List<Map<String, String>>) config.get("gamesList");
//		for (Map<String, String> tMap : gamesList) {
//			List<Games> tList = getGamesList(Integer.parseInt(tMap.get("varLen")), tMap.get("type"));
//			data.put(tMap.get("varName"), tList);
//		}
//
//		// capturecontent
//		this.getCapturecontent(data, config, template);
//		// �Ż���Ϸ�б�
//		this.getIndexGameList(data, config, template);
//		// user contribution
//		this.getUserContributionPage(data, config, template);
//		return data;
//	}
//
//	private void getContentDataFromDatabase(Map<String, Object> config, Templates template) throws Exception {
//		Map<String, Object> data = new HashMap<String, Object>();
//		// placard
//		Map<String, String> contentData = (Map<String, String>) config.get("contentData");
//		List<Map<String, String>> plaList = (List<Map<String, String>>) config.get("placardList");
//		for (Map<String, String> tMap : plaList) {
//			List<Placards> tList = getPlacardsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"), true);
//			for (Placards tp : tList) {
//				if (tp.getContentflag() == 0) {
//					tp.setPlacardslinkurl(domain + "/c/" + tp.getPlacardid() + tMap.get("postfix"));
//				}else {
//					tp.setPlacardslinkurl(tp.getPlacardcontent());
//				}
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// adv
//		List<Map<String, String>> advList = (List<Map<String, String>>) config.get("advList");
//		for (Map<String, String> tMap : advList) {
//			List<Advs> tList = getAdvsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			data.put(tMap.get("varName"), tList);
//		}
//		// resource
//		List<Map<String, String>> resList = (List<Map<String, String>>) config.get("resList");
//		for (Map<String, String> tMap : resList) {
//			List<Resources> tList = resourcesDao.getResourcesList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			for (Resources tr : tList) {
//				setThunderUrl(tr, "#");
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// include
//		List<Map<String, String>> includeList = (List<Map<String, String>>) config.get("includeList");
//		for (Map<String, String> tMap : includeList) {
//			String script = getInclude(tMap.get("path"), tMap.get("encoding"));
//			data.put(tMap.get("varName"), script);
//		}
//
//		// capturecontent
//		this.getCapturecontent(data, config, template);
//
//		// �Ż���Ϸ�б�
//		this.getIndexGameList(data, config, template);
//		// user contribution
//		this.getUserContributionPage(data, config, template);
//
//		if(contentData != null) {
//			List<Placards> ttList = getPlacardsContentList(Integer.parseInt(contentData.get("varLen")), template.getGameid(), contentData.get("type"),
//					isAll);
//			String staticPath = config.get("staticPath").toString();
//			int len = ttList.size();
//			for (int i = 0; i < len; i++) {
//				if (ttList.get(i).getPlacardid().length() > 14) {
//					continue;
//				}
//				data.put(contentData.get("varName"), ttList.get(i));
//				if (ttList.get(i).getPlacardid().length() < 8) {
//					data.put("pre", Integer.parseInt(ttList.get(i).getPlacardid()) - 1);
//					data.put("next", Integer.parseInt(ttList.get(i).getPlacardid()) + 1);
//				}
//				data.put("prePage", (i - 1 >= 0 ? ttList.get(i - 1).getPlacardid() : ttList.get(len - 1).getPlacardid()) + contentData.get("postfix"));
//				data.put("nextPage", (i + 1 < len ? ttList.get(i + 1).getPlacardid() : ttList.get(0).getPlacardid()) + contentData.get("postfix"));
//				config.put("staticPath", staticPath + ttList.get(i).getPlacardid() + contentData.get("postfix"));
//				generatePage(config, data);
//			}
//		}
//		data.clear();
//		data = null;
//	}
//
//	private void getListDataFromDatabase(Map<String, Object> config, Templates template) throws Exception {
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, String> listData = (Map<String, String>) config.get("listPageData");
//		int pageSize = Integer.parseInt(listData.get("pageSize"));
//		String[] types = listData.get("type").split(",");
//		// adv
//		List<Map<String, String>> advList = (List<Map<String, String>>) config.get("advList");
//		for (Map<String, String> tMap : advList) {
//			List<Advs> tList = getAdvsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			data.put(tMap.get("varName"), tList);
//		}
//		// resource
//		List<Map<String, String>> resList = (List<Map<String, String>>) config.get("resList");
//		for (Map<String, String> tMap : resList) {
//			List<Resources> tList = resourcesDao.getResourcesList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			for (Resources tr : tList) {
//				setThunderUrl(tr, "#");
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// include
//		List<Map<String, String>> includeList = (List<Map<String, String>>) config.get("includeList");
//		for (Map<String, String> tMap : includeList) {
//			String script = getInclude(tMap.get("path"), tMap.get("encoding"));
//			data.put(tMap.get("varName"), script);
//		}
//		// placard
//		List<Map<String, String>> plaList = (List<Map<String, String>>) config.get("placardList");
//		for (Map<String, String> tMap : plaList) {
//			List<Placards> tList = getPlacardsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"), true);
//			for (Placards tp : tList) {
//				if (tp.getContentflag() == 0) {
//					tp.setPlacardslinkurl(domain + "/c/" + tp.getPlacardid() + tMap.get("postfix"));
//				}else {
//					tp.setPlacardslinkurl(tp.getPlacardcontent());
//				}
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//
//		// capturecontent
//		this.getCapturecontent(data, config, template);
//
//		// �Ż���Ϸ�б�
//		this.getIndexGameList(data, config, template);
//		
//		// user contribution
//		this.getUserContributionPage(data, config, template);
//
//		String staticPath = config.get("staticPath").toString();
//		for (String type : types) {
//			List<Placards> newsPlacards;
//			if (type.split("-").length > 1) {// ����ϲ���list
//				String[] tmps = type.split("-");
//				String queryTypes = "";
//				for (String s : tmps) {
//					if (StringTools.isEmpty(queryTypes)) {
//						queryTypes += "'" + s + "'";
//					}else {
//						queryTypes += "," + "'" + s + "'";
//					}
//				}
//				newsPlacards = getPlacardsList(-1, template.getGameid(), queryTypes, true);
//			}else {
//				newsPlacards = getPlacardsList(-1, template.getGameid(), type, true);
//			}
//			data.put("classId", type);
//			data.put("pageName", domain + "/c/" + type);
//			List<String> pages = new ArrayList<String>();
//			if (newsPlacards.size() < 1) {
//				pages.add("1");
//				data.put("pageMax", 1);
//				data.put("pages", pages);
//				data.put("pageIndex", 1);
//				data.put("pageNext", 1);
//				data.put("pageProv", 1);
//				data.put("pageMin", 1);
//				data.put("newsPlacards", newsPlacards);
//				config.put("staticPath", staticPath + type + "_1" + listData.get("postfix"));
//				generatePage(config, data);
//			}else {
//				int pageMax = newsPlacards.size() % pageSize == 0 ? newsPlacards.size() / pageSize : (newsPlacards.size() / pageSize + 1);
//				data.put("pageMax", pageMax);
//				data.put("pageMin", 1);
//
//				int pageIndex = 1;
//				int pageSum = newsPlacards.size() % pageSize == 0 ? newsPlacards.size() / pageSize : (newsPlacards.size() / pageSize + 1);
//
//				for (pageIndex = 1; pageIndex <= pageSum; pageIndex++) {
//					int listpages = LIST_PRE_PAGES + LIST_POS_PAGES + 1;
//					pages = new ArrayList<String>();
//					for (int i = 1; i <= pageSum; i++) {
//						if (pageIndex <= LIST_PRE_PAGES) {
//							if (i == listpages + 1) {
//								pages.add("...");
//							}else if (i <= listpages) {
//								pages.add(String.valueOf(i));
//							}
//						}else if (pageIndex > pageSum - LIST_POS_PAGES) {
//							if (i == pageSum - listpages) {
//								pages.add("...");
//							}else if (i > pageSum - listpages) {
//								pages.add(String.valueOf(i));
//							}
//						}else {
//							if (i == pageIndex - (LIST_PRE_PAGES + 1) || i == pageIndex + LIST_POS_PAGES + 1) {
//								pages.add("...");
//							}else if (i >= pageIndex - LIST_PRE_PAGES && i <= pageIndex + LIST_POS_PAGES) {
//								pages.add(String.valueOf(i));
//							}
//						}
//					}
//					data.put("pages", pages);
//
//					List<Placards> newsPlacardsTmp = new ArrayList<Placards>();
//					for (int i = (pageIndex - 1) * pageSize; i < (pageIndex * pageSize > newsPlacards.size() ? newsPlacards.size() : pageIndex
//							* pageSize); i++)
//						newsPlacardsTmp.add(newsPlacards.get(i));
//					for (Placards tp : newsPlacardsTmp) {
//						if (tp.getContentflag() == 0) {
//							tp.setPlacardslinkurl(domain + "/c/" + tp.getPlacardid() + listData.get("postfix"));
//						}else {
//							tp.setPlacardslinkurl(tp.getPlacardcontent());
//						}
//					}
//					data.put("newsPlacards", newsPlacardsTmp);
//					data.put("pageIndex", pageIndex);
//					data.put("pageNext", pageIndex + 1);
//					data.put("pageProv", pageIndex - 1);
//					config.put("staticPath", staticPath + type + "_" + pageIndex + listData.get("postfix"));
//					generatePage(config, data);
//				}
//			}
//		}
//		data.clear();
//		data = null;
//	}
//
//	private void getDesktoppicDataFromDatabase(Map<String, Object> config, Templates template) throws Exception {
//		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, String> listData = (Map<String, String>) config.get("listPageData");
//		int pageSize = Integer.parseInt(listData.get("pageSize"));
//		String[] types = listData.get("type").split(",");
//		// adv
//		List<Map<String, String>> advList = (List<Map<String, String>>) config.get("advList");
//		for (Map<String, String> tMap : advList) {
//			List<Advs> tList = getAdvsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			data.put(tMap.get("varName"), tList);
//		}
//		// resource
//		List<Map<String, String>> resList = (List<Map<String, String>>) config.get("resList");
//		for (Map<String, String> tMap : resList) {
//			List<Resources> tList = resourcesDao.getResourcesList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"));
//			for (Resources tr : tList) {
//				setThunderUrl(tr, "#");
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// placard
//		List<Map<String, String>> plaList = (List<Map<String, String>>) config.get("placardList");
//		for (Map<String, String> tMap : plaList) {
//			List<Placards> tList = getPlacardsList(Integer.parseInt(tMap.get("varLen")), template.getGameid(), tMap.get("type"), true);
//			for (Placards tp : tList) {
//				if (tp.getContentflag() == 0) {
//					tp.setPlacardslinkurl(domain + "/c/" + tp.getPlacardid() + tMap.get("postfix"));
//				}else {
//					tp.setPlacardslinkurl(tp.getPlacardcontent());
//				}
//			}
//			data.put(tMap.get("varName"), tList);
//		}
//		// include
//		List<Map<String, String>> includeList = (List<Map<String, String>>) config.get("includeList");
//		for (Map<String, String> tMap : includeList) {
//			String script = getInclude(tMap.get("path"), tMap.get("encoding"));
//			data.put(tMap.get("varName"), script);
//		}
//
//		// capturecontent
//		this.getCapturecontent(data, config, template);
//
//		// �Ż���Ϸ�б�
//		this.getIndexGameList(data, config, template);
//		
//		// user contribution
//		this.getUserContributionPage(data, config, template);
//
//		String staticPath = config.get("staticPath").toString();
//		for (String type : types) {
//			List<Wallpapers> newsPlacards = wallpapersDao.getWallpapersList(-1, template.getGameid());
//			List<String> pages = new ArrayList<String>();
//			data.put("classId", type);
//			data.put("pageName", domain + "/c/" + type);
//			if (newsPlacards.size() < 1) {
//				pages.add("1");
//				data.put("pageMax", 1);
//				data.put("pages", pages);
//				data.put("pageIndex", 1);
//				data.put("pageNext", 1);
//				data.put("pageProv", 1);
//				data.put("pageMin", 1);
//				data.put("newsPlacards", newsPlacards);
//				config.put("staticPath", staticPath + type + "_1" + listData.get("postfix"));
//				generatePage(config, data);
//			}else {
//				int pageMax = newsPlacards.size() % pageSize == 0 ? newsPlacards.size() / pageSize : (newsPlacards.size() / pageSize + 1);
//				data.put("pageMax", pageMax);
//				data.put("pageMin", 1);
//
//				int pageIndex = 1;
//				int pageSum = newsPlacards.size() % pageSize == 0 ? newsPlacards.size() / pageSize : (newsPlacards.size() / pageSize + 1);
//				for (int i = 1; i <= pageSum; i++)
//					pages.add(String.valueOf(i));
//				data.put("pages", pages);
//				for (pageIndex = 1; pageIndex <= pageSum; pageIndex++) {
//					List<Wallpapers> newsPlacardsTmp = new ArrayList<Wallpapers>();
//					for (int i = (pageIndex - 1) * pageSize; i < (pageIndex * pageSize > newsPlacards.size() ? newsPlacards.size() : pageIndex
//							* pageSize); i++)
//						newsPlacardsTmp.add(newsPlacards.get(i));
//					data.put("newsPlacards", newsPlacardsTmp);
//					data.put("pageIndex", pageIndex);
//					data.put("pageNext", pageIndex + 1);
//					data.put("pageProv", pageIndex - 1);
//					config.put("staticPath", staticPath + type + "_" + pageIndex + listData.get("postfix"));
//					generatePage(config, data);
//				}
//			}
//		}
//		data.clear();
//		data = null;
//	}
//
//	/**
//	 * ʹ��velocity�������ҳ��
//	 * 
//	 * @param config
//	 * @param data
//	 * @throws Exception
//	 */
//	private void generatePage(Map<String, Object> config, Map<String, Object> data) throws Exception {
//		VelocityEngine velocityEngine = new VelocityEngine();
//		velocityEngine.setProperty("input.encoding", config.get("pageEncoding"));
//		velocityEngine.setProperty("output.encoding", config.get("pageEncoding"));
//		velocityEngine.setProperty("file.resource.loader.path", templateUploadPath);
//		velocityEngine.init();
//		Template template = velocityEngine.getTemplate(config.get("templatePath").toString().split(";")[0]);
//		StringWriter writer = new StringWriter();
//		VelocityContext context = new VelocityContext(data);
//		template.merge(context, writer);
//		String content = writer.toString();
//		File file = new File(config.get("staticPath").toString());
//		Miscellaneous.createFile(config.get("staticPath").toString());
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), config.get("pageEncoding").toString()));
//		bw.write(content);
//		bw.close();
//		velocityEngine = null;
//		template = null;
//		context = null;
//		writer = null;
//	}
//
//	// common
//	private void setThunderUrl(Resources pro, String resTitle) {
//		if (pro.getThunderurlflag()) {
//			String thunderURL = com.xunlei.owpp.util.ThunderUtil.ThunderEncode(pro.getResurl());
//			pro.setResurl("<a  href='#' thunderHref='" + thunderURL + "' thunderPid='06308' thunderResTitle='" + resTitle
//					+ "' thunderType='07' onClick='return OnDownloadClick_Simple(this)' oncontextmenu='ThunderNetwork_SetHref(this)' reseqid='"
//					+ pro.getSeqid() + "'>");
//		}else {
//			pro.setResurl("<a  href='" + pro.getResurl() + "' reseqid='" + pro.getSeqid() + "'>");
//		}
//	}
//
//	public List<Advs> getAdvsList(int num, String gameid, String types) {
//		String sql = " and FlatNo='" + gameid + "' ";
//		if (isNotEmpty(types) && !types.equals("all")) {
//			sql += " and PlaceId in(" + types + ") ";
//		}
//		sql += " and inuse = 1 ";// ���˳���Ч��
//		sql += " and (ExpireTime >= '" + DatetimeUtil.today() + "' || ExpireTime = '') ";// ���˹�ʱ�Ĺ��
//		sql += " order by displayorder asc,inputtime desc ";
//		if (num >= 0) {
//			sql += " limit 0," + num;
//		}
//		ArrayList<Advs> list = new ArrayList<Advs>(advsDao.findPagedObjects(new Advs(), sql, null).getDatas());
//		for (Advs advs : list) {
//			advs.setPicurl(img_domain + "/advs/images/" + advs.getPicurl());
//		}
//		return list;
//	}
//
//	public List<Placards> getPlacardsList(int num, String gameid, String types, boolean isAll) {
//		String sql = " and FlatNo='" + gameid + "' ";
//		if (isNotEmpty(types) && !types.equals("all")) {
//			sql += " and ClassId in(" + types + ") ";
//		}
//		if (!isAll) {
//			sql += " and (inputtime >'" + DatetimeUtil.today() + "' or edittime>'" + DatetimeUtil.today() + "')";
//		}
//		sql += " and inUse = 1 and Deleted = 0 ";// ���˳���Ч��
//		sql += " order by displayorder asc,issuedate desc,inputtime desc ";
//		if (num >= 0) {
//			sql += " limit 0," + num;
//		}
//		ArrayList<Placards> list = new ArrayList<Placards>(placardsDao.findPagedObjects(new Placards(), sql, null).getDatas());
//		for (Placards placard : list) {
//			String fckpath = contextpath + "/upload/" + gameid;
//			placard.setPlacardcontent(placard.getPlacardcontent().replaceAll(fckpath, img_domain));
//			placard.setPicurl(placard.getPicurl().split("\\,")[0]);
//		}
//		return list;
//	}
//
//	public List<Placards> getPlacardsContentList(int num, String gameid, String types, boolean isAll) {
//		String sql = " and FlatNo='" + gameid + "' ";
//		if (isNotEmpty(types) && !types.equals("all")) {
//			sql += " and ClassId in(" + types + ") ";
//		}
//		if (!isAll) {
//			sql += " and (inputtime >'" + DatetimeUtil.today() + "' or edittime>'" + DatetimeUtil.today() + "')";
//		}
//		sql += " and inUse = 1 and Deleted = 0 and Contentflag=0 ";// ���˳���Ч��
//		sql += " order by classid asc ,displayorder asc,issuedate desc,inputtime desc ";
//		if (num >= 0) {
//			sql += " limit 0," + num;
//		}
//		ArrayList<Placards> list = new ArrayList<Placards>(placardsDao.findPagedObjects(new Placards(), sql, null).getDatas());
//		for (Placards placard : list) {
//			String fckpath = contextpath + "/upload/" + gameid;
//			placard.setPlacardcontent(placard.getPlacardcontent().replaceAll(fckpath, img_domain));
//			placard.setPicurl(placard.getPicurl().split("\\,")[0]);
//		}
//		return list;
//	}
//
//	private String getInclude(String path, String encoding) throws Exception {
//		FileInputStream is = new FileInputStream(path);
//		ByteArrayOutputStream sw = new ByteArrayOutputStream();
//		byte[] buffer = new byte[BUFFERSIE];
//		int len = -1;
//		while ((len = is.read(buffer)) != -1) {
//			sw.write(buffer, 0, len);
//		}
//		is.close();
//		String res = sw.toString(isNotEmpty(encoding) ? encoding : "UTF-8");
//		sw.close();
//		return res;
//	}
//
//	private List<Games> getGamesList(int num, String types) {
//		Games data = new Games();
//		data.setClassno(types);
//		data.setInuse((short) 1);
//		data.setIsgame((short) 1);
//		PagedFliper fliper = new PagedFliper();
//		fliper.setPageSize(num);
//		fliper.setSortColumn("displayorderonnavigation asc");
//		return (List<Games>) gamesDao.getGamesView(data, fliper).getDatas();
//	}
//
//	@Override
//	public void updateGameserversInfo(String gameid) {
//		Games tg = gamesDao.getGameInfoById(gameid);
//		try {
//			this.transformServersInfo(tg);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// /************accesser********************/
//	public ITemplatesDao getTemplatesDao() {
//		return templatesDao;
//	}
//
//	public void setTemplatesDao(ITemplatesDao templatesDao) {
//		this.templatesDao = templatesDao;
//	}
//
//	public IGamesDao getGamesDao() {
//		return gamesDao;
//	}
//
//	public void setGamesDao(IGamesDao gamesDao) {
//		this.gamesDao = gamesDao;
//	}
//
//	public IVoteDao getVoteDao() {
//		return voteDao;
//	}
//
//	public void setVoteDao(IVoteDao voteDao) {
//		this.voteDao = voteDao;
//	}
//
//	public IResourcesDao getResourcesDao() {
//		return resourcesDao;
//	}
//
//	public void setResourcesDao(IResourcesDao resourcesDao) {
//		this.resourcesDao = resourcesDao;
//	}
//
//	public IWallpapersDao getWallpapersDao() {
//		return wallpapersDao;
//	}
//
//	public void setWallpapersDao(IWallpapersDao wallpapersDao) {
//		this.wallpapersDao = wallpapersDao;
//	}
//
//	public IPlacardsDao getPlacardsDao() {
//		return placardsDao;
//	}
//
//	public void setPlacardsDao(IPlacardsDao placardsDao) {
//		this.placardsDao = placardsDao;
//	}
//
//	public IAdvsDao getAdvsDao() {
//		return advsDao;
//	}
//
//	public void setAdvsDao(IAdvsDao advsDao) {
//		this.advsDao = advsDao;
//	}
//
//	public IGameserversBo getGameserversBo() {
//		return gameserversBo;
//	}
//
//	public void setGameserversBo(IGameserversBo gameserversBo) {
//		this.gameserversBo = gameserversBo;
//	}
//
//	public IGameserversstatusDao getGameserversstatusDao() {
//		return gameserversstatusDao;
//	}
//
//	public void setGameserversstatusDao(IGameserversstatusDao gameserversstatusDao) {
//		this.gameserversstatusDao = gameserversstatusDao;
//	}
//
//	public ICaptureDao getCaptureDao() {
//		return captureDao;
//	}
//
//	public void setCaptureDao(ICaptureDao captureDao) {
//		this.captureDao = captureDao;
//	}
//
//}
