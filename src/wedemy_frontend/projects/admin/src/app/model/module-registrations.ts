import { BaseEntity } from "@bootcamp-core/models/base-entity";
import { DetailClasses } from "./dtl-classes";
import { Modules } from "./modules";

export class ModuleRegistrations extends BaseEntity {
    public idDetailClass: DetailClasses = new DetailClasses();

    public idModule: Modules = new Modules();
}