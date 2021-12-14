package es.udc.ws.races.model.inscription;

import es.udc.ws.util.configuration.ConfigurationParametersManager;


public class SqlInscriptionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlInscriptionDaoFactory.className";
    private static SQLInscriptionDao dao = null;

    private SqlInscriptionDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SQLInscriptionDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SQLInscriptionDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SQLInscriptionDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }

}
