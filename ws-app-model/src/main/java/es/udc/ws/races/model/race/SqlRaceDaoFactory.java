package es.udc.ws.races.model.race;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRaceDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRaceDaoFactory.className";
    private static SQLRaceDao dao = null;

    private SqlRaceDaoFactory(){
    }

    @SuppressWarnings("rawtypes")
    private static SQLRaceDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SQLRaceDao) daoClass.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized static SQLRaceDao getDao() {

        if(dao == null){
            dao = getInstance();
        }
        return dao;
    }


}
