package org.example;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.OptionDescriptor;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class EmbASPManager {
    private static EmbASPManager instance;
    private Handler handler;
    private InputProgram program;
    private DesktopService service;
    private OptionDescriptor option;


    private EmbASPManager() {//costruttore privato
        service = new DLV2DesktopService("DropAndMerge/lib/dlv2.exe");
        handler = new DesktopHandler(service);
        option = new OptionDescriptor("-n 1");
        handler.addOption(option);
        program = new ASPInputProgram();
    }

    public static EmbASPManager getInstance() {
        if (instance == null) {
            instance = new EmbASPManager();
        }
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    public InputProgram getProgram() {
        return program;
    }
    public void reset(){
        handler.removeAll();
        handler=new DesktopHandler(service);
        handler.addOption(option);
        program= new ASPInputProgram();
    }

}
